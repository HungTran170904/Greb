package com.greb.fleetmanagementservice.model;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestHistory {
    @Id @UuidGenerator
    private String id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(nullable=false)
    private VehicleRequest vehicleRequest;

    private String changeInfo;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private String driverId;

    private String adminId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
