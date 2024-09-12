package com.greb.kafkastreamservice.utils;

import com.greb.kafkastreamservice.dtos.PositionDto;
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

    public double countBearing(PositionDto p1, PositionDto p2) {
        double lon1Rad = Math.toRadians(p1.getLongitude());
        double lon2Rad = Math.toRadians(p2.getLongitude());
        double lat1Rad = Math.toRadians(p1.getLatitude());
        double lat2Rad = Math.toRadians(p2.getLatitude());

        double x = Math.cos(lat1Rad)*Math.sin(lat2Rad) -
                Math.sin(lat1Rad)*Math.cos(lat2Rad)*Math.cos(lon2Rad-lon1Rad);
        double y= Math.sin(lon2Rad-lon1Rad) * Math.cos(lat2Rad);

        return Math.atan2(y, x);
    }
}
