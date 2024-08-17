package com.greb.fleetmanagementservice.model;

import com.greb.fleetmanagementservice.model.enums.RequestStatus;
import com.greb.fleetmanagementservice.model.enums.VehicleStatus;
import com.greb.fleetmanagementservice.model.enums.VehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {
    @Id @UuidGenerator
    private String id;

    // vehicle info
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String licencePlate;

    private String licencePlateImageId;

    private String vehicleImageId;

    private String color;

    private String manufacturer;

    private String model;

    private LocalDate registrationDate;

    private Integer maxPassengers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="serviceTypeId", nullable = false)
    private ServiceType serviceType;

    private String description;

    // request info
    private String evidenceId;

    private RequestStatus status;

    private String rejectReason;

    private LocalDateTime createdAt;
}
