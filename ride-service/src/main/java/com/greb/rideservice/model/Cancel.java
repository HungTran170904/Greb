package com.greb.rideservice.model;

import com.greb.rideservice.model.enums.CancelSource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
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

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime cancelTime;
}
