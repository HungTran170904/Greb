package com.greb.locationservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.greb.locationservice.models.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputLocation {
    private Position position;

    private String rideId;

    private String serviceId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Date time;
}
