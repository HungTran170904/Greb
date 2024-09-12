package com.greb.rideservice.models;

import com.greb.rideservice.models.enums.CancelSource;
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
public class Cancel {
    @Id @UuidGenerator
    private String id;

    private String reason;

    private CancelSource cancelSource;

    private String cancellerId; //userId

    @OneToOne
    @JoinColumn(nullable = false)
    private Ride ride;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime cancelTime;
}
