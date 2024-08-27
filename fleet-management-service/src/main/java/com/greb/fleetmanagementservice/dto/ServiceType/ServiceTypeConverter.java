package com.greb.fleetmanagementservice.dto.ServiceType;

import com.greb.fleetmanagementservice.model.ServiceType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceTypeConverter {
    private final ModelMapper modelMapper;

    public ServiceType fromRequestDto(ReqServiceTypeDto dto){
        return modelMapper.map(dto, ServiceType.class);
    }

    public ResServiceTypeDto toResponseDto(ServiceType serviceType){
        return modelMapper.map(serviceType, ResServiceTypeDto.class);
    }

    public void updateServiceType(ServiceType serviceType, ReqServiceTypeDto dto){
        serviceType.setDescription(dto.getDescription());
        serviceType.setName(dto.getName());
        serviceType.setAcceptedVehicleTypes(dto.getAcceptedVehicleTypes());
        serviceType.setBaseFare(dto.getBaseFare());
        serviceType.setDistanceFare(dto.getDistanceFare());
        serviceType.setTaxFare(dto.getTaxFare());
        serviceType.setCancellationFee(dto.getCancellationFee());
        serviceType.setChangeDestinationFee(dto.getChangeDestinationFee());
        serviceType.setIconId(dto.getIconId());
        serviceType.setIsAvailable(dto.getIsAvailable());
    }
}
