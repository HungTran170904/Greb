package com.greb.fleetmanagementservice.service;

import com.greb.fleetmanagementservice.dto.Pagination;
import com.greb.fleetmanagementservice.dto.Vehicle.ListVehiclesDto;
import com.greb.fleetmanagementservice.dto.Vehicle.ResponseVehicleDto;
import com.greb.fleetmanagementservice.dto.Vehicle.VehicleConverter;
import com.greb.fleetmanagementservice.exception.BadRequestException;
import com.greb.fleetmanagementservice.model.Vehicle;
import com.greb.fleetmanagementservice.model.enums.VehicleType;
import com.greb.fleetmanagementservice.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepo;
    private final VehicleConverter vehicleConverter;
    private final UserService userService;

    public ResponseVehicleDto getVehicleById(String vehicleId) {
        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(()-> new BadRequestException("Vehicle not found"));
        return vehicleConverter.toResponseDto(vehicle);
    }

    public ListVehiclesDto searchVehicles(
            VehicleType vehicleType,
            String licencePlate,
            String manufacturer,
            Boolean isAvailable,
            Integer pageNo,
            Integer pageSize
    ){
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        var vehiclePage= vehicleRepo.searchVehicles(
                vehicleType,
                licencePlate.toLowerCase(),
                manufacturer.toLowerCase(),
                isAvailable,
                pageRequest
        );
        var driverDtos= vehiclePage.getContent().stream()
                .map(vehicle->vehicleConverter.toResponseDto(vehicle))
                .toList();
        var pagination= new Pagination(pageNo,vehiclePage.getTotalPages(), vehiclePage.getTotalElements());
        return new ListVehiclesDto(driverDtos,pagination);
    }

    public List<ResponseVehicleDto> getByDriverId(){
        String driverId= userService.getDriverId();
        return vehicleRepo.findByDriverId(driverId).stream()
                .map(vehicle->vehicleConverter.toResponseDto(vehicle))
                .toList();
    }
}
