package com.greb.fleetmanagementservice.dto.VehicleRequest;

import com.greb.fleetmanagementservice.dto.Pagination;

import java.util.List;

public record ListVehicleRequestsDto(
        List<ResVehicleRequestDto> vehicleRequests,
        Pagination pagination
) {}
