package com.greb.rideservice.dto.Cancel;

import com.greb.rideservice.model.enums.CancelSource;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseCancelDto {
    private String id;

    private String reason;

    private CancelSource cancelSource;

    private LocalDateTime cancelTime;
}
