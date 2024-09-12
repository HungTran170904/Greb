package com.greb.rideservice.dtos.RideAddress;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRideAddressDto {
    public enum Type {
        PICKUP_ADDRESS,
        DROPOFF_ADDRESS
    }

    @NotNull @Valid
    private ReqRideAddressDto newAddress;

    @NotNull
    private Double distance;

    private Type type;

    @NotNull
    private String rideId;
}
