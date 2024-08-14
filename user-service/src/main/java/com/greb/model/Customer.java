package com.greb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id @UuidGenerator
    private String id;

    private String address;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private Boolean gender;

    private Integer discountPoint=0;

    @Column(unique=true, nullable = false)
    private String userId;

    private String avatarId;
}
