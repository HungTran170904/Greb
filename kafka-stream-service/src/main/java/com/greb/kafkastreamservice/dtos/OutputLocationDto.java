package com.greb.kafkastreamservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OutputLocationDto {
    private String driverId;

    private PositionDto position;

    private DriverStatus status;

    private String vehicleId;

    private String serviceTypeId;

    private Integer timestamp;

    private Double rideDistance;

    private Double bearing;

    private Double instantSpeed;
}
