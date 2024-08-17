package com.greb.fleetmanagementservice.dto.ServiceType;

import com.greb.fleetmanagementservice.model.enums.VehicleType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ResServiceTypeDto {
    private String id;

    private String name;

    private String description;

    private BigDecimal baseFare;

    private BigDecimal distanceFare;

    private BigDecimal taxFare;

    private BigDecimal timeFare;

    private BigDecimal cancellationFee;

    private List<VehicleType> acceptedVehicleTypes;

    private Boolean isAvailable;

    private String iconId;
}
