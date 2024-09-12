package com.greb.locationservice.controllers;

import com.greb.locationservice.models.Location;
import com.greb.locationservice.services.LocationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
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
public class LocationController {
    private final LocationService outputLocationService;

    @GetMapping("/private/search-best-vehicles")
    public ResponseEntity<Location> searchBestVehicles(
            @RequestParam("userLon") Double userLon,
            @RequestParam("userLat") Double userLat,
            @RequestParam("serviceId") @UUID String serviceId,
            @RequestParam("maxDistance") Double maxDistance,
            @RequestParam("rejectedDriverIds") String rejectedDriverIdsStr
    ) {
        return ResponseEntity.ok(
                outputLocationService.searchBestVehicles(
                        userLon,
                        userLat,
                        serviceId,
                        maxDistance,
                        rejectedDriverIdsStr)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search-near-vehicles")
    public ResponseEntity<List<Location>> searchNearVehicles(
            @RequestParam(value = "choosedLon", required = false) Double choosedLon,
            @RequestParam(value = "choosedLat", required = false) Double choosedLat,
            @RequestParam(value = "serviceId", required = false) @UUID String serviceId,
            @RequestParam(value = "maxDistance", required = false) Double maxDistance,
            @RequestParam(value = "isOnline", required = false) Boolean isOnline
    ) {
        return ResponseEntity.ok(
                outputLocationService.searchNearVehicles(
                        choosedLon,
                        choosedLat,
                        serviceId,
                        maxDistance,
                        isOnline)
        );
    }

    @GetMapping("/vehicle-ids")
    public ResponseEntity<List<Location>> findByVehicleIds(
            @RequestParam("vehicleIds") String vehicleIdsStr
    ){
        var vehicleIds= Arrays.stream(vehicleIdsStr.split(",")).toList();
        return ResponseEntity.ok(outputLocationService.findByVehicleIds(vehicleIds));
    }
}
