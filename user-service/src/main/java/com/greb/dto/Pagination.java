package com.greb.dto;

public record Pagination (
    Integer currentPage,
    Integer totalPages,
    Long totalRecords
){}
