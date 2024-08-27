package com.greb.rideservice.dto.Rating;

import com.greb.rideservice.model.Ride;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class RequestRatingDto {
    @NotNull @UUID
    private String rideId;

    @NotNull @Min(0) @Max(5)
    private int rate;

    private String comment;

    private String driverId;

    private String customerId;
}
