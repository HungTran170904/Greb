package com.greb.fleetmanagementservice.dto.ServiceType;

import com.greb.fleetmanagementservice.model.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ReqServiceTypeDto {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private BigDecimal baseFare;

    @NotNull
    private BigDecimal distanceFare;

    @NotNull
    private BigDecimal taxFare;

    @NotNull
    private BigDecimal timeFare;

    @NotNull
    private BigDecimal cancellationFee;

    @NotNull
    private List<VehicleType> acceptedVehicleTypes;

    @NotNull
    private Boolean isAvailable=false;

    private String iconId;
}
