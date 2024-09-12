package com.greb.rideservice.utils;

import com.greb.rideservice.models.RideCost;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CostUtil {
    public void calculateTotalCost(RideCost cost){
        BigDecimal totalCost = BigDecimal.ZERO;
        if(cost.getBaseFare()!=null)
            totalCost= totalCost.add(cost.getBaseFare());
        if(cost.getCancellationFee()!=null)
            totalCost= totalCost.add(cost.getCancellationFee());
        if(cost.getChangeDestinationFee()!=null)
            totalCost= totalCost.add(cost.getChangeDestinationFee());
        if(cost.getDiscount()!=null)
            totalCost= totalCost.add(cost.getDiscount());
        if(cost.getTollFare()!=null)
            totalCost= totalCost.add(cost.getTollFare());
        if(cost.getDistanceFare()!=null)
            totalCost= totalCost.add(cost.getDistanceFare());
       cost.setTotalCost(totalCost);
    }
}
