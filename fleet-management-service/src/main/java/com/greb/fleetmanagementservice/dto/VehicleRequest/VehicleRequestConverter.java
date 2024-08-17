package com.greb.fleetmanagementservice.dto.VehicleRequest;

import com.greb.fleetmanagementservice.model.VehicleRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleRequestConverter {
    private final ModelMapper modelMapper;

    public VehicleRequest fromReqDto(CreateVehicleRequestDto dto) {
        return modelMapper.map(dto, VehicleRequest.class);
    }

    public void updateVehicleRequest(VehicleRequest vehicleRequest, UpdateVehicleRequestDto dto){
        vehicleRequest.setLicencePlate(dto.getLicencePlate());
        vehicleRequest.setModel(dto.getModel());
        vehicleRequest.setDescription(dto.getDescription());
        vehicleRequest.setManufacturer(dto.getManufacturer());
        vehicleRequest.setMaxPassengers(dto.getMaxPassengers());
        vehicleRequest.setLicencePlateImageId(dto.getLicencePlateImageId());
        vehicleRequest.setRegistrationDate(dto.getRegistrationDate());
        vehicleRequest.setModel(dto.getModel());
    }

    public ResVehicleRequestDto toResponseDto(VehicleRequest vehicleRequest) {
        var dto= modelMapper.map(vehicleRequest, ResVehicleRequestDto.class);
        if(vehicleRequest.getVehicle()!=null)
            dto.setVehicleId(vehicleRequest.getVehicle().getId());
        return dto;
    }
}
