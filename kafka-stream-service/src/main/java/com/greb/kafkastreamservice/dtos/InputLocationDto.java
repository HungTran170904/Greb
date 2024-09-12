package com.greb.kafkastreamservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class InputLocationDto {
    private PositionDto position;

    private String rideId;

    private String serviceId;

    private String driverId;

    private String vehicleId;

    private Integer timestamp; //miliseconds unit
}
