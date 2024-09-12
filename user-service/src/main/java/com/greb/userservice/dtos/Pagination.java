package com.greb.userservice.dtos;

public record Pagination (
    Integer currentPage,
    Integer totalPages,
    Long totalRecords
){}
