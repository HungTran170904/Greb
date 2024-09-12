package com.greb.userservice.dtos.Driver;

import com.greb.userservice.dtos.Pagination;
import java.util.List;

public record ListDriversDto (
    List<ResponseDriverDto> drivers,
    Pagination pagination
){}
