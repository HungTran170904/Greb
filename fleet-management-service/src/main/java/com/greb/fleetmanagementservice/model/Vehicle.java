package com.greb.fleetmanagementservice.model;

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
public class Vehicle {
    @Id @UuidGenerator
    private String id;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column(nullable=false)
    private String licencePlate;

    private String licencePlateImageId;

    private String vehicleImageId;

    private String color;

    private String manufacturer;

    private String model;

    private LocalDate registrationDate;

    private Integer maxPassengers;

    @Column(nullable = false)
    private String driverId;

    private Boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ServiceType serviceType;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
