package com.greb.fleetmanagementservice.service;

import com.greb.fleetmanagementservice.dto.ServiceType.ReqServiceTypeDto;
import com.greb.fleetmanagementservice.dto.ServiceType.ResServiceTypeDto;
import com.greb.fleetmanagementservice.dto.ServiceType.ServiceTypeConverter;
import com.greb.fleetmanagementservice.exception.BadRequestException;
import com.greb.fleetmanagementservice.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceTypeService {
    private final ServiceTypeRepository serviceTypeRepo;
    private final ServiceTypeConverter serviceTypeConverter;

    public ResServiceTypeDto createServiceType(ReqServiceTypeDto dto){
        if(serviceTypeRepo.existsByName(dto.getName()))
            throw new BadRequestException("A service type with name '"+dto.getName()+"' has already existed");
        var serviceType= serviceTypeConverter.fromRequestDto(dto);
        var savedServiceType=serviceTypeRepo.save(serviceType);
        return serviceTypeConverter.toResponseDto(savedServiceType);
    }

    public ResServiceTypeDto updateServiceType(String serviceTypeId, ReqServiceTypeDto dto){
        var serviceType= serviceTypeRepo.findById(serviceTypeId)
                .orElseThrow(()->new BadRequestException("Updated Service Type not found"));
        serviceTypeConverter.updateServiceType(serviceType, dto);
        serviceTypeRepo.save(serviceType);
        return serviceTypeConverter.toResponseDto(serviceType);
    }

    public ResServiceTypeDto getById(String serviceTypeId){
        var serviceType= serviceTypeRepo.findById(serviceTypeId)
                .orElseThrow(()->new BadRequestException("ServiceTypeId "+serviceTypeId+" not found"));
        return serviceTypeConverter.toResponseDto(serviceType);
    }

    public List<ResServiceTypeDto> getAll(){
        var serviceTypes= serviceTypeRepo.findAll();
        return serviceTypes.stream()
                .map(serviceType -> serviceTypeConverter.toResponseDto(serviceType))
                .toList();
    }
}
