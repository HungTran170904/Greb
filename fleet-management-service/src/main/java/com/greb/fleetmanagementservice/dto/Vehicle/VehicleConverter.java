package com.greb.fleetmanagementservice.dto.Vehicle;

import com.greb.fleetmanagementservice.model.ServiceType;
import com.greb.fleetmanagementservice.model.Vehicle;
import com.greb.fleetmanagementservice.model.VehicleRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleConverter {
    private final ModelMapper modelMapper;

    public Vehicle fromVehicleRequest(VehicleRequest vehicleRequest) {
        return modelMapper.map(vehicleRequest, Vehicle.class);
    }
    public void updateVehicle(Vehicle vehicle, VehicleRequest vehicleRequest){
        if(vehicleRequest.getColor()!=null)
            vehicle.setColor(vehicleRequest.getColor());
        if(vehicleRequest.getModel()!=null)
            vehicle.setModel(vehicleRequest.getModel());
        if(vehicleRequest.getLicencePlate()!=null)
            vehicle.setLicencePlate(vehicleRequest.getLicencePlate());
        if(vehicleRequest.getVehicleImageId()!=null)
            vehicle.setVehicleImageId(vehicleRequest.getVehicleImageId());
        if(vehicleRequest.getManufacturer()!=null)
            vehicle.setManufacturer(vehicleRequest.getManufacturer());
        if(vehicleRequest.getVehicleImageId()!=null)
            vehicle.setVehicleImageId(vehicleRequest.getVehicleImageId());
        if(vehicleRequest.getMaxPassengers()!=null)
            vehicle.setMaxPassengers(vehicleRequest.getMaxPassengers());
        if(vehicleRequest.getRegistrationDate()!=null)
            vehicle.setRegistrationDate(vehicleRequest.getRegistrationDate());
        if(vehicleRequest.getVehicleType()!=null)
            vehicle.setVehicleType(vehicleRequest.getVehicleType());
    }

    public ResponseVehicleDto toResponseDto(Vehicle vehicle){
        var dto= modelMapper.map(vehicle, ResponseVehicleDto.class);
        if(vehicle.getServiceType()!=null)
            dto.setServiceTypeId(vehicle.getServiceType().getId());
        return dto;
    }
}
