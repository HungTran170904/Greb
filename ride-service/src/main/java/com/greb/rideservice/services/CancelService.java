package com.greb.rideservice.services;

import com.greb.rideservice.dtos.Cancel.CancelConverter;
import com.greb.rideservice.dtos.Cancel.RequestCancelDto;
import com.greb.rideservice.dtos.Ride.ResponseRideDto;
import com.greb.rideservice.dtos.Ride.RideConverter;
import com.greb.rideservice.exceptions.BadRequestException;
import com.greb.rideservice.models.Cancel;
import com.greb.rideservice.models.Ride;
import com.greb.rideservice.models.enums.CancelSource;
import com.greb.rideservice.models.enums.RideStatus;
import com.greb.rideservice.repositories.CancelRepository;
import com.greb.rideservice.repositories.RideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CancelService {
    private final CancelRepository cancelRepo;
    private final RideRepository rideRepo;
    private final RideConverter rideConverter;
    private final CancelConverter cancelConverter;
    private final WebsocketService websocketService;

    @Transactional
    public Ride cancelRideBySystem(Ride ride, String reason){
        var cancel= Cancel.builder()
                .cancelSource(CancelSource.SYSTEM)
                .reason(reason)
                .build();
        ride.setCancel(cancel);

        ride.setStatus(RideStatus.CANCELLED);
        var savedRide= rideRepo.save(ride);

        websocketService.notifyRideCancelToCustomer(
                ride.getCustomerId(),
                cancelConverter.toDto(savedRide.getCancel()));
        return savedRide;
    }

    public ResponseRideDto cancelRideByUser(RequestCancelDto dto){
        var authentication= SecurityContextHolder.getContext().getAuthentication();
        var userId= authentication.getName();

        var ride= rideRepo.findById(dto.getRideId())
                .orElseThrow(()-> new BadRequestException("Ride not found"));
        if(ride.getCancel()!=null)
            throw new BadRequestException("The ride has already been cancelled");
        ride.setStatus(RideStatus.CANCELLED);

        var cancel= Cancel.builder()
                .cancellerId(userId)
                .reason(dto.getReason())
                .ride(ride)
                .build();
        ride.setCancel(cancel);

        List<String> roles= authentication.getAuthorities()
                .stream().map(grantedAuthority ->
                        grantedAuthority.getAuthority()
                                .replace("ROLE_","")
                ).toList();
        if(roles.contains("DRIVER"))
            cancel.setCancelSource(CancelSource.DRIVER);
        else if(roles.contains("CUSTOMER"))
            cancel.setCancelSource(CancelSource.CUSTOMER);
        else throw new BadRequestException("Invalid user");

        var savedRide=rideRepo.save(ride);
        var resRideDto= rideConverter.toResponseDto(savedRide);

        if(roles.contains("DRIVER"))
            websocketService.notifyRideCancelToCustomer(
                    savedRide.getCustomerId(),
                    resRideDto.getCancel());
        else if(roles.contains("CUSTOMER"))
            websocketService.notifyRideCancelToDriver(
                    savedRide.getCustomerId(),
                    resRideDto.getCancel());

        return resRideDto;
    }
}
