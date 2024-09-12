package com.greb.rideservice.dtos.RideCost;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;

@Data
public class TollFareDto {
    @NotBlank @UUID
    private String rideId;

    @NotNull @DecimalMin("0.00")
    private BigDecimal tollFare;
}
