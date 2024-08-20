package com.greb.locationservice.dto;

public record Pagination(
        Integer currentPage,
        Integer totalPages,
        Long totalReocrds
) {
}
