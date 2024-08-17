package com.greb.dto.Customer;

import com.greb.dto.Pagination;

import java.util.List;

public record ListCustomersDto (
        List<ResponseCustomerDto> custoemrs,
        Pagination pagination
){}
