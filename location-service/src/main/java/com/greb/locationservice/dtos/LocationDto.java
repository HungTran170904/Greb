package com.greb.locationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDto {
    private String driverId;

    private PositionDto position;

    private String vehicleId;

    private String serviceTypeId;

    private DriverStatus status;

    private Integer timestamp;

    private Double rideDistance;

    private Double bearing;

    private Double instantSpeed;
}
