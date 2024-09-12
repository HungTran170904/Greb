package com.greb.rideservice.controllers;

import com.greb.rideservice.dtos.RideAddress.ChangeRideAddressDto;
import com.greb.rideservice.services.RideAddressService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RideAddressController {
    private final RideAddressService rideAddressService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/rides/pickUpAddress")
    public ResponseEntity<Void> changePickUpAddress(
            @RequestBody @Valid ChangeRideAddressDto dto
    ){
        rideAddressService.changePickupAddress(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/rides/dropUpAddress")
    public ResponseEntity<Void> changeDropUpAddress(
            @RequestBody @Valid ChangeRideAddressDto dto
    ){
        rideAddressService.changeDropOffAddress(dto);
        return ResponseEntity.noContent().build();
    }
}
