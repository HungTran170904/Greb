package com.greb.rideservice.dtos.Cancel;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestCancelDto {
    @NotBlank
    private String rideId;

    @NotBlank
    private String reason;
}
