package com.greb.rideservice.dtos.Ride;

import com.greb.rideservice.dtos.Pagination;

import java.util.List;

public record ListRidesDto (
    List<ResponseRideDto> rides,
    Pagination pagination
){}
