package com.greb.rideservice.dtos.Driver;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
@NotNull
public class DriverDetailDto {
    private String driverId;

    private String firstName;

    private String lastName;

    private String avatarId;

    private String vehicleId;

    private String licencePlateNumber;

    private String vehicleColor;

    private String vehicleImageId;

    private Double avarageRating;
}
