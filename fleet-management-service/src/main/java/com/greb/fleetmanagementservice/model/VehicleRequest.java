package com.greb.fleetmanagementservice.model;

import com.greb.fleetmanagementservice.model.enums.RequestStatus;
import com.greb.fleetmanagementservice.model.enums.RequestType;
import com.greb.fleetmanagementservice.model.enums.VehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleRequest {
    @Id @UuidGenerator
    private String id;

    @Column(nullable = false)
    private String driverId;

    // vehicle info
    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    private ServiceType serviceType;

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

    // request info
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    private RequestStatus status;

    private String description;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
