package com.greb.controller;

import com.greb.dto.Driver.RegisterDriverDto;
import com.greb.dto.Driver.ResponseDriverDto;
import com.greb.dto.Driver.UpdateDriverDto;
import com.greb.model.enums.JobStatus;
import com.greb.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register")
    public ResponseEntity<ResponseDriverDto> register(
            @Valid @RequestBody RegisterDriverDto dto
    ){
        return ResponseEntity.ok(driverService.register(dto));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/update-profile")
    public ResponseEntity<ResponseDriverDto> updateProfile(
            @Valid @RequestBody UpdateDriverDto dto
    ){
        return ResponseEntity.ok(driverService.updateProfile(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/accept-driver/{driverId}")
    public ResponseEntity<Void> acceptDriver(
            @PathVariable("driverId") String driverId,
            @RequestParam("jobStatus") JobStatus jobStatus
    ){
        driverService.acceptDriver(driverId, jobStatus);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/profile")
    public ResponseEntity<ResponseDriverDto> getProfile(){
        return ResponseEntity.ok(driverService.getProfile());
    }
}
