package com.greb.rideservice.dtos.Cancel;

import com.greb.rideservice.models.enums.CancelSource;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseCancelDto {
    private String id;

    private String reason;

    private CancelSource cancelSource;

    private String cancellerId;

    private LocalDateTime cancelTime;
}
