package com.greb.rideservice.services;

import com.greb.rideservice.constraints.KafkaTopics;
import com.greb.rideservice.dtos.Location.DriverStatus;
import com.greb.rideservice.dtos.Pagination;
import com.greb.rideservice.dtos.Ride.*;
import com.greb.rideservice.dtos.RideAddress.RideAddressConverter;
import com.greb.rideservice.dtos.RideCost.ResRideCostDto;
import com.greb.rideservice.dtos.RideCost.RideCostConverter;
import com.greb.rideservice.dtos.RideCost.TollFareDto;
import com.greb.rideservice.exceptions.BadRequestException;
import com.greb.rideservice.models.RideCost;
import com.greb.rideservice.models.enums.RideStatus;
import com.greb.rideservice.repositories.RideRepository;
import com.greb.rideservice.utils.CostUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
@Slf4j
public class RideService {
    private final RideConverter rideConverter;
    private final RideAddressConverter rideAddressConverter;
    private final RideCostConverter rideCostConverter;
    private final CancelService cancelService;
    private final UserService userService;
    private final FleetManagementService fleetManagementService;
    private final DriverSearchService searchDriverService;
    private final WebsocketService websocketService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RideRepository rideRepo;
    private final CostUtil costUtil;

    @Transactional
    public ResponseRideDto createRide(RequestRideDto dto){
        var ride=rideConverter.fromRequestDto(dto);
        ride.setStatus(RideStatus.PENDING);
        ride.setCustomerId(userService.getCustomerId());

        var pickUpAddress= rideAddressConverter.fromRequestDto(dto.getPickUpAddress());
        ride.setPickUpAddress(pickUpAddress);

        var dropOffAddress= rideAddressConverter.fromRequestDto(dto.getDropOffAddress());
        ride.setDropOffAddress(dropOffAddress);

        var serviceType= fleetManagementService.getServiceTypeById(ride.getServiceTypeId());
        RideCost rideCost= RideCost.builder()
                .paymentMethod(dto.getPaymentMethod())
                .baseFare(serviceType.getBaseFare())
                .distanceFare(
                        serviceType.getDistanceFare()
                                .multiply(BigDecimal.valueOf(ride.getDistance()))
                )
                .taxFare(serviceType.getTaxFare())
                .build();
        costUtil.calculateTotalCost(rideCost);
        ride.setRideCost(rideCost);

        var savedRide=rideRepo.save(ride);
        var rideDto=rideConverter.toResponseDto(savedRide);
        CompletableFuture.runAsync(()->searchDriverService.searchDriver(ride));
        return rideDto;
    }

    public ResponseRideDto checkRideStatus(String rideId){
        var ride= rideRepo.findById(rideId)
                .orElseThrow(()-> new BadRequestException("Ride Id "+rideId+" not found"));
        var driverSearch= ride.getDriverSearch();
        var now= new Date();
        if(driverSearch.getExpiredTime().getTime()> now.getTime()+3000){
            ride=cancelService.cancelRideBySystem(ride, "Unknown error");
        }
        return rideConverter.toResponseDto(ride);
    }

    @Transactional
    public void acceptRideByDriver(AcceptRideDto dto){
        var driverDetail= dto.getDriverDetail();
        var ride= rideRepo.findById(dto.getRideId())
                .orElseThrow(()-> new BadRequestException("Ride Id "+dto.getRideId()+" not found"));
        if(ride.getStatus()!= RideStatus.PENDING)
            throw new BadRequestException("Ride "+dto.getRideId()+" is not in status "+RideStatus.PENDING);

        var driverSearch= ride.getDriverSearch();
        if(
                driverSearch.getCurrChoosedDriverId()==null||
                !driverSearch.getCurrChoosedDriverId()
                        .equals(driverDetail.getDriverId())
        )
            throw new BadRequestException("You were not choosed for this ride");

        var now= new Date();
        if(driverSearch.getExpiredTime().getTime()> now.getTime()+3000)
            throw new BadRequestException("Ride "+dto.getRideId()+" is expired");

        ride.setStatus(RideStatus.COMING);
        ride.setVehicleId(driverDetail.getVehicleId());
        ride.setDriverId(driverDetail.getDriverId());
        ride.setAcceptTime(LocalDateTime.now());
        rideRepo.save(ride);
        websocketService.notifyRideAcceptingToCustomer(ride.getCustomerId(), dto);
    }

    @Transactional
    public void startRideByDriver(String rideId){
        var ride= rideRepo.findById(rideId)
                .orElseThrow(()-> new BadRequestException("Ride Id "+rideId+" not found"));

        var driverId= userService.getDriverId();
        if(!ride.getDriverId().equals(driverId))
            throw new BadRequestException("You do not have permission to start this ride");

        ride.setStatus(RideStatus.HAPPENING);
        ride.setStartTime(LocalDateTime.now());
        rideRepo.save(ride);
        kafkaTemplate.send(KafkaTopics.rideEventTopic,driverId, DriverStatus.ACTIVE.name());
    }

    @Transactional
    public ResRideCostDto finishRideByDriver(FinishRideDto dto){
        if(dto.getNextStatus()!=DriverStatus.OFFLINE
                &&dto.getNextStatus()!=DriverStatus.AVAILABLE)
            throw new BadRequestException("Next Status must be OFFLINE or AVAILABLE");
        var ride= rideRepo.findById(dto.getRideId())
                .orElseThrow(()-> new BadRequestException("Ride Id "+dto.getRideId()+" not found"));

        var driverId= userService.getDriverId();
        if(!ride.getDriverId().equals(driverId))
            throw new BadRequestException("You do not have permission to start this ride");

        ride.setStatus(RideStatus.FINISHED);
        ride.setEndTime(LocalDateTime.now());
        rideRepo.save(ride);
        websocketService.notifyRideFinishedToCustomer(ride.getCustomerId(),dto.getRideId());
        kafkaTemplate.send(KafkaTopics.rideEventTopic,driverId, dto.getNextStatus().name());
        return rideCostConverter.toResponseDto(ride.getRideCost());
    }

    public ListRidesDto getRidesForCustomer(
            Integer pageNo,
            Integer pageSize
    ){
        var customerId= userService.getCustomerId();
        var pageRequest= PageRequest.of(pageNo, pageSize);
        var ridesPage= rideRepo.getRidesByCustomerId(customerId, pageRequest);
        var listRideDtos= ridesPage.stream().map(rideConverter::toResponseDto).toList();
        Pagination pagination= new Pagination(
                pageNo,
                ridesPage.getTotalPages(),
                ridesPage.getTotalElements()
        );
        return new ListRidesDto(listRideDtos, pagination);
    }

    public ListRidesDto getRidesForDriver(
            Integer pageNo,
            Integer pageSize
    ){
        var driverId= userService.getDriverId();
        var pageRequest= PageRequest.of(pageNo, pageSize);
        var ridesPage= rideRepo.getRidesByDriverId(driverId, pageRequest);
        var listRideDtos= ridesPage.stream().map(rideConverter::toResponseDto).toList();
        Pagination pagination= new Pagination(
                pageNo,
                ridesPage.getTotalPages(),
                ridesPage.getTotalElements()
        );
        return new ListRidesDto(listRideDtos, pagination);
    }
}
