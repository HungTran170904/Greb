package com.greb.rideservice.dtos.Ride;

import com.greb.rideservice.dtos.RideAddress.ReqRideAddressDto;
import com.greb.rideservice.models.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestRideDto {
    @NotNull
    private ReqRideAddressDto pickUpAddress;

    @NotNull
    private ReqRideAddressDto dropOffAddress;

    @NotNull
    private Double distance; //km

    @NotNull
    private PaymentMethod paymentMethod;

    @NotBlank
    private String serviceTypeId;

    @NotBlank
    private String coordinatesPath;
}
