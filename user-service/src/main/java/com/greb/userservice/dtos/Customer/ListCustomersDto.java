package com.greb.userservice.dtos.Customer;

import com.greb.userservice.dtos.Pagination;

import java.util.List;

public record ListCustomersDto (
        List<ResponseCustomerDto> custoemrs,
        Pagination pagination
){}
