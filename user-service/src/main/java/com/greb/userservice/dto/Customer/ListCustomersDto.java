package com.greb.userservice.dto.Customer;

import com.greb.userservice.dto.Pagination;

import java.util.List;

public record ListCustomersDto (
        List<ResponseCustomerDto> custoemrs,
        Pagination pagination
){}
