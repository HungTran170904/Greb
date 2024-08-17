package com.greb.fleetmanagementservice.dto.VehicleRequest;

import com.greb.fleetmanagementservice.model.VehicleRequest;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestHistoryDto {
    private String id;

    private VehicleRequest vehicleRequest;

    private String changeInfo;

    private String comment;

    private String userId;

    private LocalDateTime createdAt;
}
