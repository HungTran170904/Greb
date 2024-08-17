package com.greb.controller;

import com.greb.dto.Driver.ListDriversDto;
import com.greb.dto.Driver.RegisterDriverDto;
import com.greb.dto.Driver.ResponseDriverDto;
import com.greb.dto.Driver.UpdateDriverDto;
import com.greb.dto.ErrorDto;
import com.greb.model.enums.JobStatus;
import com.greb.service.DriverService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Access Denied",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content(schema = @Schema(implementation = ResponseDriverDto.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<ResponseDriverDto> register(
            @Valid @RequestBody RegisterDriverDto dto
    ){
        return ResponseEntity.ok(driverService.register(dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Access Denied",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content(schema = @Schema(implementation = ResponseDriverDto.class)))
    })
    @PreAuthorize("hasRole('DRIVER')")
    @PutMapping("/update-profile")
    public ResponseEntity<ResponseDriverDto> updateProfile(
            @Valid @RequestBody UpdateDriverDto dto
    ){
        return ResponseEntity.ok(driverService.updateProfile(dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Access Denied",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "204", description = "Ok")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/accept-driver/{driverId}")
    public ResponseEntity<Void> acceptDriver(
            @PathVariable("driverId") String driverId,
            @RequestParam("jobStatus") JobStatus jobStatus
    ){
        driverService.acceptDriver(driverId, jobStatus);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-rating/{driverId}")
    public ResponseEntity<Void> updateRating(
            @PathVariable("driverId") String driverId,
            @RequestParam("ratingPoint") @Min(0) @Max(5) Integer ratingPoint
    ){
        driverService.updateAvarageRating(driverId, ratingPoint);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Access Denied",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content(schema = @Schema(implementation = ResponseDriverDto.class)))
    })
    @PreAuthorize("hasAnyRole('DRIVER','ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<ResponseDriverDto> getProfile(){
        return ResponseEntity.ok(driverService.getProfile());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<ListDriversDto> getByJobStatus(
            @RequestParam(value="name",defaultValue = "", required = false) String name,
           @RequestParam(value="jobStatus", required=false) JobStatus jobStatus,
           @RequestParam("pageNo") @Min(0) Integer pageNo,
           @RequestParam("pageSize") @Min(1) Integer pageSize
    ){
        return ResponseEntity.ok(driverService.searchDrivers(name,jobStatus, pageNo, pageSize));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/private/driver-id")
    public ResponseEntity<String> getDriverId(){
        return ResponseEntity.ok(driverService.getDriverId());
    }
}
