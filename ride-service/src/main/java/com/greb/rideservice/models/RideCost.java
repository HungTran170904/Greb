package com.greb.rideservice.models;

import com.greb.rideservice.models.enums.PaymentMethod;
import com.greb.rideservice.models.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideCost {
    @Id @UuidGenerator
    private String id;

    @OneToOne
    private Ride ride;

    private BigDecimal cancellationFee;

    private BigDecimal baseFare;

    private BigDecimal distanceFare;

    private BigDecimal taxFare;

    private BigDecimal discount;

    private BigDecimal changeDestinationFee;

    private BigDecimal tollFare;

    private BigDecimal totalCost;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private String paymentId;
}

