package com.greb.locationservice.utils;

import com.greb.locationservice.dtos.PositionDto;
import org.springframework.stereotype.Component;

@Component
public class PositionUtil {
    private static final double EARTH_RADIUS= 6378.137;

    public double countDistance(PositionDto p1, PositionDto p2) {
        double lon1Rad = Math.toRadians(p1.getLongitude());
        double lon2Rad = Math.toRadians(p2.getLongitude());
        double lat1Rad = Math.toRadians(p1.getLatitude());
        double lat2Rad = Math.toRadians(p2.getLatitude());


        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        double distance = Math.sqrt(x * x + y * y) * EARTH_RADIUS;

        return distance;
    }
}
