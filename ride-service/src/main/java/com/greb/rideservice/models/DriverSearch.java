package com.greb.rideservice.models;

import com.greb.rideservice.models.converters.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverSearch {
    @Id @UuidGenerator
    private String id;

    @OneToOne
    @JoinColumn(nullable = false)
    private Ride ride;

    @Convert(converter= StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> rejectedDriverIds;

    private String currChoosedDriverId;

    private Integer searchRound;

    private Date expiredTime;
}
