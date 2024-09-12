package com.greb.rideservice.dtos.Ride;

import com.greb.rideservice.dtos.Cancel.CancelConverter;
import com.greb.rideservice.dtos.RideAddress.RideAddressConverter;
import com.greb.rideservice.models.Ride;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideConverter {
    private final ModelMapper modelMapper;
    private final CancelConverter cancelConverter;
    private final RideAddressConverter rideAddressConverter;

    public Ride fromRequestDto(RequestRideDto dto){
        return modelMapper.map(dto, Ride.class);
    }

    public ResponseRideDto toResponseDto(Ride ride){
        var dto=modelMapper.map(ride, ResponseRideDto.class);
        if(ride.getCancel() != null){
            dto.setCancel(cancelConverter.toDto(ride.getCancel()));
        }
        if(ride.getPickUpAddress()!=null){
            dto.setPickUpAddress(rideAddressConverter
                    .toResponseDto(ride.getPickUpAddress()));
        }
        if(ride.getPrePickUpAddress()!=null){
            dto.setPrePickUpAddress(rideAddressConverter
                    .toResponseDto(ride.getPrePickUpAddress()));
        }
        if(ride.getDropOffAddress()!=null){
            dto.setDropOffAddress(rideAddressConverter
                    .toResponseDto(ride.getDropOffAddress()));
        }
        if(ride.getPreDropOffAddress()!=null){
            dto.setPreDropOffAddress(rideAddressConverter
                    .toResponseDto(ride.getPreDropOffAddress()));
        }
        return dto;
    }
}
