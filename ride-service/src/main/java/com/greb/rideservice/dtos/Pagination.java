package com.greb.rideservice.dtos;

public record Pagination(
    Integer currentPage,
    Integer totalPages,
    Long totalRecords
){}
