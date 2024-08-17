package com.greb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class Customer {
    @Id @UuidGenerator
    private String id;

    private String name;

    private String address;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private Boolean gender;

    private Integer discountPoint=0;

    @Column(unique=true, nullable = false)
    private String userId;

    private String avatarId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
