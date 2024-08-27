package com.greb.locationservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Position {
    private Double longitude;

    private Double latitude;
}
