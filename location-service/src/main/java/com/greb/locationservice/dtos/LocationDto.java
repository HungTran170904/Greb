package com.greb.locationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutputLocationDto {
    private PositionDto position;

    private String rideId;

    private String serviceTypeId;

    private String driverId;

    private String vehicleId;

    private Integer timestamp;

    private Double rideDistance;

    private Double bearing;

    private Double instantSpeed;
}
