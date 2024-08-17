package com.greb.fleetmanagementservice.dto.RequestHistory;

import com.greb.fleetmanagementservice.model.VehicleRequest;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestHistoryDto {
    private String id;

    private String vehicleRequestId;

    private String changeInfo;

    private String comment;

    private String adminId;

    private String driverId;

    private LocalDateTime createdAt;
}
