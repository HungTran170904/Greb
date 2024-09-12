package com.greb.locationservice.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.greb.locationservice.dtos.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "location")
public class Location {
    @Id
    private String driverId;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint position;

    private DriverStatus status;

    private String vehicleId;

    private String serviceTypeId;

    private Integer timestamp;

    private Double rideDistance;

    private Double bearing;

    private Double instantSpeed;
}
