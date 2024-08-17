package com.greb.fleetmanagementservice.model;

import com.greb.fleetmanagementservice.model.converters.VehicletypeListConverter;
import com.greb.fleetmanagementservice.model.enums.VehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceType {
    @Id @UuidGenerator
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    private BigDecimal baseFare;

    private BigDecimal distanceFare;

    private BigDecimal taxFare;

    private BigDecimal timeFare;

    private BigDecimal cancellationFee;

    @Column(columnDefinition = "TEXT")
    @Convert(converter=VehicletypeListConverter.class)
    private List<VehicleType> acceptedVehicleTypes;

    private Boolean isAvailable;

    private String iconId;
}
