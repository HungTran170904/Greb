package com.greb.rideservice.dtos.RideAddress;

import lombok.Data;

@Data
public class ResRideAddressDto {
    private String id;

    private Double latitude;

    private Double longitude;

    private String formattedAddress;

    private String placeId;
}
