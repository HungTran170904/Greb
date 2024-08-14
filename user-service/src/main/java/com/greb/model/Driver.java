package com.greb.model;

import com.greb.model.enums.JobStatus;
import jakarta.persistence.*;
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
public class Driver {
    @Id @UuidGenerator
    private String id;

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
}
