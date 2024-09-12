package com.greb.rideservice.dtos.Ride;

import com.greb.rideservice.dtos.Driver.DriverDetailDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AcceptRideDto {
    @NotBlank
    private String rideId;

    @Valid
    @NotNull
    private DriverDetailDto driverDetail;
}
