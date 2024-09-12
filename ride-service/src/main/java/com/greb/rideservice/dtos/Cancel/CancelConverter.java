package com.greb.rideservice.dtos.Cancel;

import com.greb.rideservice.models.Cancel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancelConverter {
    private final ModelMapper modelMapper;

    public ResponseCancelDto toDto(Cancel cancel) {
       return modelMapper.map(cancel, ResponseCancelDto.class);
    }
}
