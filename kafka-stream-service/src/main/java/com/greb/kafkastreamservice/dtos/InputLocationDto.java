package com.greb.kafkastreamservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InputLocationDto {
    private DriverStatus status;

    private PositionDto position;

    private String vehicleId;

    private String serviceTypeId;

    private Integer timestamp; //miliseconds unit
}
