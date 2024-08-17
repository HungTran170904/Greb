package com.greb.fleetmanagementservice.dto.Vehicle;

import com.greb.fleetmanagementservice.model.enums.VehicleStatus;
import com.greb.fleetmanagementservice.model.enums.VehicleType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseVehicleDto {
    private String id;

    private VehicleType vehicleType;

    private String licencePlate;

    private String licencePlateImageId;

    private String vehicleImageId;

    private String color;

    private String manufacturer;

    private String model;

    private LocalDate registrationDate;

    private VehicleStatus status;

    private Integer maxPassengers;

    private String serviceTypeId;
}
