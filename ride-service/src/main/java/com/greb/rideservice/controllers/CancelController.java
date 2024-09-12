package com.greb.rideservice.controllers;

import com.greb.rideservice.dtos.Cancel.RequestCancelDto;
import com.greb.rideservice.dtos.Ride.ResponseRideDto;
import com.greb.rideservice.services.CancelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CancelController {
    private final CancelService cancelService;

    @PreAuthorize("hasAnyRole('DRIVER','CUSTOMER')")
    @PostMapping("/cancels")
    public ResponseEntity<ResponseRideDto> cancelRide(
            @RequestBody @Valid RequestCancelDto dto
    ){
        return ResponseEntity.ok(cancelService.cancelRideByUser(dto));
    }
}
