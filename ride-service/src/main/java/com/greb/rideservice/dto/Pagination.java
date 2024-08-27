package com.greb.rideservice.dto;

public record Pagination(
    Integer currentPage,
    Integer totalPages,
    Long totalRecords
){}
