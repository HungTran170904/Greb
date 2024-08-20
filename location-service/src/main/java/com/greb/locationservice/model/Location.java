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
public class Location {
    @Id @UuidGenerator
    private String id;

    private String name;

    @
    private Address address;

    private Float lat;

    private Float lng;

    private String website;

    private Integer rating;
}
