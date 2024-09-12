package com.greb.rideservice.controllers;

import com.greb.rideservice.dtos.RideCost.EstimatedCostDto;
import com.greb.rideservice.dtos.RideCost.TollFareDto;
import com.greb.rideservice.services.CostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CostController {
    private final CostService costService;

    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/rides/ride-cost/tollfare")
    public ResponseEntity<Void> addTollFare(
            @RequestBody @Valid TollFareDto dto
    ){
        costService.addTollFare(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rides/estimated-cost")
    public ResponseEntity<List<EstimatedCostDto>> getEstimatedCost(
            @RequestParam("distance") Double distance
    ){
        return ResponseEntity.ok(costService.getEstimatedCost(distance));
    }
}
