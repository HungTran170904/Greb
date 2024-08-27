package com.greb.rideservice.dto.Ride;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDateTime;

public class RequestRideDto {
    @NotNull @UUID
    private String vehicleId;

    @NotNull @UUID
    private String customerId;


    @NotBlank
    private String startPlaceId;


    @NotBlank
    private String endPlaceId;

    @NotNull @Min(1)
    private int distance; //km

    @NotNull
    private LocalDateTime startTime;
}
