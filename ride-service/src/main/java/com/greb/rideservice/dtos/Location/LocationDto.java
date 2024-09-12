package com.greb.rideservice.dtos.Location;

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

    private DriverStatus status;

    private String serviceTypeId;

    private String vehicleId;

    private Integer timestamp;

    private Double rideDistance;

    private Double bearing;

    private Double instantSpeed;
}
