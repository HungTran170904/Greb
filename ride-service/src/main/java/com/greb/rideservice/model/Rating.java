package com.greb.rideservice.model;

import com.greb.rideservice.model.enums.RatingSource;
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
public class Rating {
    @Id @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Ride ride;

    @Column(nullable = false)
    private int ratingStar;

    private String comment;

    @Column(nullable = false)
    private RatingSource ratingSource;

    @Column(nullable = false)
    private String raterId; //userId

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
