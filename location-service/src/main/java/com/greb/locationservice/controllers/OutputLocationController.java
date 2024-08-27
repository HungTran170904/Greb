package com.greb.locationservice.controllers;

import com.greb.locationservice.models.OutputLocation;
import com.greb.locationservice.services.OutputLocationService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/output-location")
@RequiredArgsConstructor
public class OutputLocationController {
    private final OutputLocationService outputLocationService;

    @GetMapping("/private/search-best-vehicles")
    public ResponseEntity<List<OutputLocation>> searchBestVehicles(
            @RequestParam("userLon") Double userLon,
            @RequestParam("userLat") Double userLat,
            @RequestParam("serviceId") @UUID String serviceId,
            @RequestParam("maxDistance") Double maxDistance,
            @RequestParam("limit") Integer limit
    ) {
        return ResponseEntity.ok(
                outputLocationService.searchBestVehicles(
                        userLon,
                        userLat,
                        serviceId,
                        maxDistance,
                        limit)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search-near-vehicles")
    public ResponseEntity<List<OutputLocation>> searchNearVehicles(
            @RequestParam(value = "choosedLon", required = false) Double choosedLon,
            @RequestParam(value = "choosedLat", required = false) Double choosedLat,
            @RequestParam(value = "serviceId", required = false) @UUID String serviceId,
            @RequestParam(value = "maxDistance", required = false) Double maxDistance,
            @RequestParam("limit") Integer limit
    ) {
        return ResponseEntity.ok(
                outputLocationService.searchNearVehicles(
                        choosedLon,
                        choosedLat,
                        serviceId,
                        maxDistance,
                        limit)
        );
    }

    @GetMapping("/vehicle-ids")
    public ResponseEntity<List<OutputLocation>> findByVehicleIds(
            @RequestParam("vehicleIds") String vehicleIdsStr
    ){
        var vehicleIds= Arrays.stream(vehicleIdsStr.split(",")).toList();
        return ResponseEntity.ok(outputLocationService.findByVehicleIds(vehicleIds));
    }
}
