package com.greb.rideservice.controllers;

import com.greb.rideservice.dtos.Ride.*;
import com.greb.rideservice.dtos.RideCost.TollFareDto;
import com.greb.rideservice.services.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RideController {
    private final RideService rideService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/rides")
    public ResponseEntity<ResponseRideDto> createRide(
            @RequestBody @Valid RequestRideDto dto
    ){
        return ResponseEntity.ok(rideService.createRide(dto));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/rides/{rideId}/check-search-process")
    public ResponseEntity<ResponseRideDto> checkSearchProcess(
            @PathVariable("rideId") @UUID String rideId
    ){
        return ResponseEntity.ok(rideService.checkRideStatus(rideId));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PostMapping("/rides/accepting")
    public ResponseEntity<Void> acceptRideByDriver(
            @RequestBody @Valid AcceptRideDto dto
    ){
        rideService.acceptRideByDriver(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PostMapping("/rides/{rideId}/starting")
    public ResponseEntity<Void> startRideByDriver(
            @PathVariable @UUID String rideId
    ){
        rideService.startRideByDriver(rideId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PostMapping("/rides/finishing")
    public ResponseEntity<Void> finishRideByDriver(
            @RequestBody @Valid FinishRideDto dto
    ){
        rideService.finishRideByDriver(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers/rides")
    public ResponseEntity<ListRidesDto> getRidesForCustomer(
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize
    ){
        return ResponseEntity.ok(rideService.getRidesForCustomer(pageNo, pageSize));
    }

    @GetMapping("/drivers/rides")
    public ResponseEntity<ListRidesDto> getRidesForDriver(
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize
    ){
        return ResponseEntity.ok(rideService.getRidesForDriver(pageNo, pageSize));
    }
}
