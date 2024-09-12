package com.greb.rideservice.dtos.RideCost;

import com.greb.rideservice.dtos.ServiceType.ResServiceTypeDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class EstimatedCostDto {
    private BigDecimal estimatedCost;

    private ResServiceTypeDto serviceType;
}
