package com.greb.rideservice.dtos.RideCost;

import com.greb.rideservice.models.RideCost;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideCostConverter {
    private final ModelMapper modelMapper;

    public ResRideCostDto toResponseDto(RideCost rideCost) {
        return modelMapper.map(rideCost, ResRideCostDto.class);
    }
}
