package com.greb.rideservice.dtos.RideAddress;

import com.greb.rideservice.models.RideAddress;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideAddressConverter {
    private final ModelMapper modelMapper;

    public RideAddress fromRequestDto(ReqRideAddressDto dto) {
        return modelMapper.map(dto, RideAddress.class);
    }

    public ResRideAddressDto toResponseDto(RideAddress rideAddress) {
        return modelMapper.map(rideAddress, ResRideAddressDto.class);
    }
}
