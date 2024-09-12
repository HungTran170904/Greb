package com.greb.rideservice.dtos.Ride;

import com.greb.rideservice.dtos.Cancel.ResponseCancelDto;
import com.greb.rideservice.dtos.RideAddress.ResRideAddressDto;
import com.greb.rideservice.models.enums.RideStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseRideDto {
    private String id;

    private String vehicleId;

    private String customerId;

    private ResRideAddressDto pickUpAddress;

    private ResRideAddressDto prePickUpAddress;

    private ResRideAddressDto dropOffAddress;

    private ResRideAddressDto preDropOffAddress;

    private String endPlaceId;

    private int distance; //km

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String serviceTypeId;

    private RideStatus status;

    private ResponseCancelDto cancel;

    private LocalDateTime createdAt;
}
