package com.greb.rideservice.dtos.Rating;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseRatingDto {
    private String id;

    private String rideId;

    private int rate;

    private String comment;

    private String driverId;

    private String customerId;

    private LocalDateTime createdAt;
}
