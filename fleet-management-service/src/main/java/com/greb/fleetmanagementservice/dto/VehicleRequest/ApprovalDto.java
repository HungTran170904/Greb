package com.greb.fleetmanagementservice.dto.VehicleRequest;

import com.greb.fleetmanagementservice.model.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

public record ApprovalDto(
        @UUID
        String vehicleRequestId,
        @NotNull
        RequestStatus requestStatus,
        String comment
) {
}
