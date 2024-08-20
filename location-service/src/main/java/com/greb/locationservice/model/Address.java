package com.greb.locationservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id @UuidGenerator
    private String id;

    private String street;

    private String city;

    private String state;

    private String region;

    private String province;

    private Integer postalCode;
}
