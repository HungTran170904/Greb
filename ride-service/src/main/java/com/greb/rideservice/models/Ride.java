package com.greb.rideservice.models;

import com.greb.rideservice.models.enums.RideStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {
    @Id @UuidGenerator
    private String id;

    @Column(nullable = false)
    private String vehicleId;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private String driverId;

    @OneToOne(cascade = CascadeType.ALL)
    private RideAddress prePickUpAddress;

    @OneToOne(cascade = CascadeType.ALL)
    private RideAddress pickUpAddress;

    @OneToOne(cascade = CascadeType.ALL)
    private RideAddress preDropOffAddress;

    @OneToOne(cascade = CascadeType.ALL)
    private RideAddress dropOffAddress;

    @OneToOne(mappedBy = "ride", cascade=CascadeType.ALL)
    private RideCost rideCost;

    private Double distance; //km

    private LocalDateTime acceptTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(nullable = false)
    private String serviceTypeId;

    @Column(nullable = false)
    private RideStatus status;

    @OneToOne(mappedBy = "ride",  cascade=CascadeType.ALL)
    private DriverSearch driverSearch;

    @OneToOne(mappedBy = "ride", cascade=CascadeType.ALL)
    private Cancel cancel;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
