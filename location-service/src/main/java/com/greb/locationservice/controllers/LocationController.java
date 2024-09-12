package com.greb.locationservice.controllers;

import com.greb.locationservice.dtos.LocationDto;
import com.greb.locationservice.services.LocationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/private/locations/best-location")
    public ResponseEntity<LocationDto> searchBestLocation(
            @RequestParam("userLon") Double userLon,
            @RequestParam("userLat") Double userLat,
            @RequestParam("serviceTypeId") @UUID String serviceTypeId,
            @RequestParam("maxDistance") Double maxDistance,
            @RequestParam("rejectedDriverIds") String rejectedDriverIdsStr
    ) {
        return ResponseEntity.ok(
                locationService.searchBestLocation(
                        userLon,
                        userLat,
                        serviceTypeId,
                        maxDistance,
                        rejectedDriverIdsStr)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/locations/near-locations")
    public ResponseEntity<List<LocationDto>> searchNearDrivers(
            @RequestParam(value = "choosedLon", required = false) Double choosedLon,
            @RequestParam(value = "choosedLat", required = false) Double choosedLat,
            @RequestParam(value = "serviceTypeId", required = false) @UUID String serviceTypeId,
            @RequestParam(value = "maxDistance", required = false) Double maxDistance,
            @RequestParam(value = "isOnline", required = false) Boolean isOnline
    ) {
        return ResponseEntity.ok(
                locationService.searchNearLocations(
                        choosedLon,
                        choosedLat,
                        serviceTypeId,
                        maxDistance,
                        isOnline)
        );
    }

    @GetMapping("/drivers/locations")
    public ResponseEntity<List<LocationDto>> findByDriverIds(
            @RequestParam("driverIds") String driverIdsStr
    ){
        return ResponseEntity.ok(locationService.findByDriverIds(driverIdsStr));
    }

    @GetMapping("/drivers/{driverId}/location")
    public ResponseEntity<LocationDto> findByDriverId(
            @PathVariable("driverId") String driverId
    ){
        return ResponseEntity.ok(locationService.findByDriverId(driverId));
    }
}
