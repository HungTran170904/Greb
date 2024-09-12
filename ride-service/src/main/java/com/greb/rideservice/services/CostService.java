package com.greb.rideservice.services;

import com.greb.rideservice.dtos.RideCost.EstimatedCostDto;
import com.greb.rideservice.dtos.RideCost.TollFareDto;
import com.greb.rideservice.exceptions.BadRequestException;
import com.greb.rideservice.repositories.RideRepository;
import com.greb.rideservice.utils.CostUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CostService {
    private final PaymentService paymentService;
    private final FleetManagementService fleetManagementService;
    private final RideRepository rideRepo;
    private final CostUtil costUtil;

    public void addTollFare(TollFareDto dto){
        var ride= rideRepo.findById(dto.getRideId())
                .orElseThrow(()-> new BadRequestException("Ride Id "+dto.getRideId()+" not found"));
        var cost=ride.getRideCost();
        cost.setTollFare(dto.getTollFare());
        costUtil.calculateTotalCost(cost);
        rideRepo.save(ride);
        paymentService.createPayment();
    }

    public List<EstimatedCostDto> getEstimatedCost(
            Double distance
    ){
        var serviceTypes= fleetManagementService.getAllServiceTypes();
        return serviceTypes.stream().map(serviceType->{
            var cost= serviceType.getBaseFare()
                    .add(serviceType.getTaxFare())
                    .add(
                            serviceType.getDistanceFare()
                            .multiply(BigDecimal.valueOf(distance))
                    );
            return new EstimatedCostDto(cost,serviceType);
        }).toList();
    }
}
