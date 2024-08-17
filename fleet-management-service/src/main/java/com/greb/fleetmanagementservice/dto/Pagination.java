package com.greb.fleetmanagementservice.dto;

public record Pagination(
        Integer currentPage,
        Integer totalPages,
        Long totalReocrds
) {
}
