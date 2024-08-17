package com.greb.fleetmanagementservice.dto.VehicleRequest;

import com.greb.fleetmanagementservice.model.enums.RequestStatus;
import com.greb.fleetmanagementservice.model.enums.RequestType;
import com.greb.fleetmanagementservice.model.enums.VehicleType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ResVehicleRequestDto {
    private String id;

    private String vehicleId;

    private String driverId;

    private VehicleType vehicleType;

    private String licencePlate;

    private String licencePlateImageId;

    private String vehicleImageId;

    private String color;

    private String manufacturer;

    private String model;

    private LocalDate registrationDate;

    private Integer maxPassengers;

    private String description;

    private RequestType requestType;

    private RequestStatus status;

    private String rejectReason;

    private LocalDateTime createdAt;
}
