package com.greb.userservice.dto.Driver;

import com.greb.userservice.dto.Pagination;
import java.util.List;

public record ListDriversDto (
    List<ResponseDriverDto> drivers,
    Pagination pagination
){}
