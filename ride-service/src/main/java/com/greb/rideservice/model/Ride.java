package com.greb.rideservice.model;

import com.greb.rideservice.model.enums.RideStatus;
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
    private String startPlaceId;

    @Column(nullable = false)
    private String endPlaceId;

    private int distance; //km

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(nullable = false)
    private RideStatus status;

    @OneToOne
    private Cancel cancel;

    @Column(columnDefinition = "TEXT")
    private String coordinatesPath;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
