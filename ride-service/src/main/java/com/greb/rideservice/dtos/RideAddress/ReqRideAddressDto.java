package com.greb.rideservice.dtos.RideAddress;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReqRideAddressDto {
    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotBlank
    private String formattedAddress;

    @NotBlank
    private String placeId;
}
