package com.greb.rideservice.services;

import com.greb.rideservice.configs.DriverSearchConfig;
import com.greb.rideservice.dtos.Location.LocationDto;
import com.greb.rideservice.dtos.Ride.RideConverter;
import com.greb.rideservice.models.DriverSearch;
import com.greb.rideservice.models.Ride;
import com.greb.rideservice.models.enums.RideStatus;
import com.greb.rideservice.repositories.DriverSearchRepository;
import com.greb.rideservice.repositories.RideRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class DriverSearchService {
    private final CancelService cancelService;
    private final WebsocketService websocketService;
    private final LocationService locationService;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final DriverSearchRepository driverSearchRepo;
    private final RideRepository rideRepo;
    private final RideConverter rideConverter;
    private final DriverSearchConfig driverSearchConfig;

    public void searchDriver(String rideId){
        var ride= rideRepo.findById(rideId)
                .orElseThrow(()-> new InternalException("Ride Id "+rideId+" not found"));
        searchDriver(ride);
    }

    public void searchDriver(Ride ride){
        var driverSearch= ride.getDriverSearch();
        var pickUpAddress= ride.getPickUpAddress();
        var locationDto= locationService.searchBestLocation(
                pickUpAddress.getLongitude(),
                pickUpAddress.getLatitude(),
                ride.getServiceTypeId(),
                driverSearchConfig.maxDistance(),
                driverSearch==null?null:driverSearch.getRejectedDriverIds()
        );

        if(locationDto!=null)
            sendRequestToDriverAndScheduleCheck(locationDto, ride);
        else if(driverSearch.getSearchRound()<= driverSearchConfig.maxRound())
            scheduleRetrySearchDriver(ride);
        else cancelService.cancelRideBySystem(
                ride,
               "The search has reached the max round: "+driverSearchConfig.maxRound()
            );
    }

    private boolean checkDriverAccepted(String rideId){
        var ride= rideRepo.findById(rideId)
                .orElseThrow(()-> new InternalException("Ride Id "+rideId+" not found"));
        // driver have accepted the ride
        if(ride.getStatus()!= RideStatus.PENDING) return true;

        var driverSearch= ride.getDriverSearch();
        if(driverSearch.getSearchRound()<=driverSearchConfig.maxRound())
            searchDriver(ride);
        else cancelService.cancelRideBySystem(
                ride,
                "The search has reached the max round: "+driverSearchConfig.maxRound()
        );
        return false;
    }

    private void sendRequestToDriverAndScheduleCheck(LocationDto locationDto, Ride ride){
        var driverSearch= ride.getDriverSearch();
        var now= new Date();
        var expiredTime = new Date(now.getTime() + driverSearchConfig.maxAcceptTime() * 1000);
        // driver search is not created
        if(driverSearch==null){
            driverSearch= DriverSearch.builder()
                    .rejectedDriverIds(new ArrayList())
                    .currChoosedDriverId(locationDto.getDriverId())
                    .expiredTime(expiredTime)
                    .ride(rideRepo.getById(ride.getId()))
                    .searchRound(1)
                    .build();
        }
        // driver search has been created
        else{
            if(driverSearch.getCurrChoosedDriverId()!=null){
                driverSearch.getRejectedDriverIds().add(driverSearch.getCurrChoosedDriverId());
            }
            driverSearch.setSearchRound(driverSearch.getSearchRound()+1);
            driverSearch.setCurrChoosedDriverId(locationDto.getDriverId());
            driverSearch.setExpiredTime(expiredTime);
        }

        driverSearchRepo.save(driverSearch);
        websocketService.sendRideRequestToDriver(
                locationDto.getDriverId(),
                rideConverter.toResponseDto(ride));
        taskScheduler.schedule(()->checkDriverAccepted(ride.getId()), expiredTime);
    }

    private void scheduleRetrySearchDriver(Ride ride){
        var driverSearch= ride.getDriverSearch();
        var now= new Date();
        driverSearch.setCurrChoosedDriverId(null);
        var expiredTime = new Date(now.getTime() + driverSearchConfig.retryTime() * 1000);
        driverSearch.setExpiredTime(expiredTime);
        driverSearchRepo.save(driverSearch);
        taskScheduler.schedule(()->searchDriver(ride.getId()), expiredTime);
    }
}
