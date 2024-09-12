package com.greb.rideservice.services;

import com.greb.rideservice.dtos.RideAddress.ChangeRideAddressDto;
import com.greb.rideservice.dtos.RideAddress.ReqRideAddressDto;
import com.greb.rideservice.dtos.RideAddress.RideAddressConverter;
import com.greb.rideservice.exceptions.BadRequestException;
import com.greb.rideservice.models.RideCost;
import com.greb.rideservice.models.enums.RideStatus;
import com.greb.rideservice.repositories.RideRepository;
import com.greb.rideservice.utils.CostUtil;
import com.greb.rideservice.utils.PositionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RideAddressService {
    private final WebsocketService websocketService;
    private final RideRepository rideRepo;
    private final RideAddressConverter rideAddressConverter;
    private final LocationService locationService;
    private final FleetManagementService fleetManagementService;
    private final CostUtil costUtil;

    @Value(("${ride-address.max-pickup-change-minutes}"))
    private Integer maxPickupChangeMinutes;

    @Transactional
    public void changePickupAddress(ChangeRideAddressDto dto){
        var ride= rideRepo.findById(dto.getRideId())
                .orElseThrow(()-> new BadRequestException("Ride not found"));

        if(ride.getStatus()== RideStatus.PENDING)
            throw new BadRequestException("Can't change pick up address as we are finding driver for the ride");
        if(ride.getStatus()==RideStatus.HAPPENING)
            throw new BadRequestException("Can't change pick up address as he ride is starting");
        if(ride.getStatus()==RideStatus.CANCELLED)
            throw new BadRequestException("The ride has been cancelled");
        if(ride.getStatus()==RideStatus.FINISHED)
            throw new BadRequestException("The ride has been finished");
        if(ride.getStatus()!=RideStatus.COMING)
            throw new BadRequestException("The ride is not in 'COMING' state");

        //COMING case
        var now= LocalDateTime.now();
        if(ride.getAcceptTime().plusMinutes(maxPickupChangeMinutes).isBefore(now))
            throw new BadRequestException("You can only change pick up within 5 minutes after booking the ride");

        if(ride.getPrePickUpAddress()!=null)
            throw new BadRequestException("You are only allowed to change pick up address once time");
        var newPickUpAddress= rideAddressConverter.fromRequestDto(dto.getNewAddress());

        ride.setPrePickUpAddress(ride.getPickUpAddress());
        ride.setPickUpAddress(newPickUpAddress);
        rideRepo.save(ride);

        dto.setType(ChangeRideAddressDto.Type.PICKUP_ADDRESS);
        websocketService.notifyDriverRideAddressChange(ride.getDriverId(), dto);
    }

    @Transactional
    public void changeDropOffAddress(ChangeRideAddressDto dto){
        var ride= rideRepo.findById(dto.getRideId())
                .orElseThrow(()-> new BadRequestException("Ride not found"));

        if(ride.getStatus()== RideStatus.PENDING)
            throw new BadRequestException("Can't change pick up address as we are finding driver for the ride");
        if(ride.getStatus()==RideStatus.CANCELLED)
            throw new BadRequestException("The ride has been cancelled");
        if(ride.getStatus()==RideStatus.FINISHED)
            throw new BadRequestException("The ride has been finished");

        if(ride.getPreDropOffAddress()!=null)
            throw new BadRequestException("You are only allowed to change drop off address once time");
        var newDropOffAddress= rideAddressConverter.fromRequestDto(dto.getNewAddress());
        ride.setPreDropOffAddress(ride.getDropOffAddress());
        ride.setDropOffAddress(newDropOffAddress);

        if(ride.getStatus()==RideStatus.HAPPENING){
            var location=locationService.getByDriverId(ride.getDriverId());
            dto.setDistance(dto.getDistance()+ location.getRideDistance());
        }

        var serviceType= fleetManagementService.getServiceTypeById(ride.getServiceTypeId());
        RideCost rideCost= RideCost.builder()
                .baseFare(serviceType.getBaseFare())
                .distanceFare(
                        serviceType.getDistanceFare()
                                .multiply(BigDecimal.valueOf(ride.getDistance()))
                )
                .taxFare(serviceType.getTaxFare())
                .build();
        costUtil.calculateTotalCost(rideCost);
        ride.setRideCost(rideCost);

        rideRepo.save(ride);

        dto.setType(ChangeRideAddressDto.Type.DROPOFF_ADDRESS);
        websocketService.notifyDriverRideAddressChange(ride.getDriverId(), dto);
    }
}
