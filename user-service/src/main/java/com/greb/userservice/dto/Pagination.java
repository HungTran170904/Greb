package com.greb.userservice.dto;

public record Pagination (
    Integer currentPage,
    Integer totalPages,
    Long totalRecords
){}
