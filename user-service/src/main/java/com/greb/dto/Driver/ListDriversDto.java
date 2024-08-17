package com.greb.dto.Driver;

import com.greb.dto.Pagination;
import java.util.List;

public record ListDriversDto (
    List<ResponseDriverDto> drivers,
    Pagination pagination
){}
