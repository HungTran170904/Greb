package com.greb.locationservice.dto.Location;

import lombok.Data;

@Data
public class RequestLocationDto {
    @NotB
    private String name;

    private String addressId;

    private Float lat;

    private Float lng;

    private String website;

    private Integer rating;
}
