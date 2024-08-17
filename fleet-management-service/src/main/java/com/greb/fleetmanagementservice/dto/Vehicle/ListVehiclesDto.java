package com.greb.fleetmanagementservice.dto.Vehicle;

import com.greb.fleetmanagementservice.dto.Pagination;

import java.util.List;

public record ListVehiclesDto(
        List<ResponseVehicleDto> vehciles,
        Pagination pagination
) {}
