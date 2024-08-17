package com.greb.fleetmanagementservice.dto.VehicleRequest;

import com.greb.fleetmanagementservice.model.enums.RequestType;
import com.greb.fleetmanagementservice.model.enums.VehicleType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDate;

@Data
public class ReqVehicleRequestDto {
    @UUID
    private String vehicleId;

    private VehicleType vehicleType;

    @Pattern(regexp = "^[0-9]{2}-[A-Z]{1}-[0-9]{3}\\.[0-9]{2}$", message = "Invalid licence plate")
    private String licencePlate;

    @UUID
    private String licencePlateImageId;

    @UUID
    private String vehicleImageId;

    private String color;

    private String manufacturer;

    private String model;

    private LocalDate registrationDate;

    @Min(1)
    private Integer maxPassengers;

    @UUID
    private String serviceTypeId;

    private String description;

    @NotNull
    private RequestType requestType;
}
