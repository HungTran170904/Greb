package com.greb.fleetmanagementservice.controller;

import com.greb.fleetmanagementservice.dto.Vehicle.ListVehiclesDto;
import com.greb.fleetmanagementservice.dto.Vehicle.ResponseVehicleDto;
import com.greb.fleetmanagementservice.model.enums.VehicleType;
import com.greb.fleetmanagementservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @GetMapping("/id/{vehicleId}")
    public ResponseEntity<ResponseVehicleDto> getById(
            @PathVariable("vehicleId") String vehicleId
    ){
        return ResponseEntity.ok(vehicleService.getVehicleById(vehicleId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<ListVehiclesDto> searchVehicles(
            @RequestParam(value = "vehicleType", required = false) VehicleType vehicleType,
            @RequestParam(value = "licencePlate", defaultValue = "", required = false) String licencePlate,
            @RequestParam(value = "manufacturer", defaultValue = "", required = false) String manufacturer,
            @RequestParam(value = "isAvailable", required = false) Boolean isAvailable,
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize
    ){
        var result= vehicleService.searchVehicles(
                vehicleType,
                licencePlate,
                manufacturer,
                isAvailable,
                pageNo,
                pageSize
        );
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/driver")
    public ResponseEntity<List<ResponseVehicleDto>> getByDriverId(){
        return ResponseEntity.ok(vehicleService.getByDriverId());
    }
}
