package com.greb.rideservice.dtos.Ride;

import com.greb.rideservice.dtos.Location.DriverStatus;
import lombok.Data;

@Data
public class FinishRideDto {
    private String rideId;

    private DriverStatus nextStatus;
}
