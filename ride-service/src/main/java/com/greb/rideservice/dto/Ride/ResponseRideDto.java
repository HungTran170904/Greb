package com.greb.rideservice.dto.Ride;

import com.greb.rideservice.dto.Cancel.ResponseCancelDto;
import com.greb.rideservice.model.enums.RideStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseRideDto {
    private String id;

    private String vehicleId;

    private String customerId;

    private String startPlaceId;

    private String endPlaceId;

    private int distance; //km

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private RideStatus status;

    private ResponseCancelDto cancel;

    private LocalDateTime createdAt;
}
