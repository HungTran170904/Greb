package com.greb.locationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PositionDto {
    private Double longitude;

    private Double latitude;
}
