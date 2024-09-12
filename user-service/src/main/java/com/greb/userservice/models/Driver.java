package com.greb.userservice.models;

import com.greb.userservice.models.enums.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
public class Driver {
    @Id @UuidGenerator
    private String id;

    private String name;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    private String address;

    private String licenceNumber;

    private String identityCard;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    @Column(unique=true, nullable = false)
    private String userId;

    private String avatarId;

    @Column(nullable = false)
    private Double averageRating;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
