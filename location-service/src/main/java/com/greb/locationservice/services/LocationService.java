package com.greb.locationservice.services;

import com.greb.locationservice.dtos.DriverStatus;
import com.greb.locationservice.dtos.LocationConverter;
import com.greb.locationservice.dtos.LocationDto;
import com.greb.locationservice.models.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final MongoTemplate mongoTemplate;
    private final LocationConverter locationConverter;

    public LocationDto searchBestLocation(
            Double userLon,
            Double userLat,
            String serviceTypeId,
            Double maxDistance,
            String rejectedDriverIdsStr) {
        Query query = new Query();

        var rejectedDriverIds= Arrays.stream(rejectedDriverIdsStr.split("\\,")).toList();
        if(!rejectedDriverIds.isEmpty())
            query.addCriteria(Criteria.where("driverId").not().in(rejectedDriverIds));

        GeoJsonPoint position = new GeoJsonPoint(userLon, userLat);
        query.addCriteria(Criteria.where("position")
                .near(position).maxDistance(maxDistance*1000));

        query.addCriteria(Criteria.where("serviceTypeId").is(serviceTypeId));

        query.addCriteria(Criteria.where("status").is(DriverStatus.AVAILABLE));

        var locations= mongoTemplate.find(query, Location.class);
        if(locations.isEmpty()) return null;
        else return locationConverter.toDto(locations.get(0));
    }

    public List<LocationDto> searchNearLocations(
            Double choosedLon,
            Double choosedLat,
            String serviceTypeId,
            Double maxDistance,
            Boolean isOnline
    ){
        Query query = new Query();
        if(choosedLon != null&&choosedLat!=null&&maxDistance!=null){
            GeoJsonPoint position = new GeoJsonPoint(choosedLon, choosedLat);
            query.addCriteria(Criteria.where("position")
                    .near(position).maxDistance(maxDistance*1000));
        }
        if(serviceTypeId!=null){
            query.addCriteria(Criteria.where("serviceTypeId").is(serviceTypeId));
        }
        if(isOnline!=null){
            if(isOnline)
                query.addCriteria(Criteria.where("status").is(DriverStatus.ACTIVE));
            else query.addCriteria(Criteria.where("status").not().is(DriverStatus.ACTIVE));
        }
        var locations= mongoTemplate.find(query, Location.class);
        return locations.stream().map(locationConverter::toDto).toList();
    }

    public List<LocationDto> findByDriverIds(String driverIdsStr) {
        var driverIds= Arrays.stream(driverIdsStr.split("\\,")).toList();
        Query query = new Query();
        query.addCriteria(Criteria.where("driverId").in(driverIds));
        var locations= mongoTemplate.find(query, Location.class);
        return locations.stream().map(locationConverter::toDto).toList();
    }

    public LocationDto findByDriverId(String driverId){
        Query query = new Query();
        query.addCriteria(Criteria.where("driverId").is(driverId));
        var location= mongoTemplate.findOne(query, Location.class);
        if(location!=null) return locationConverter.toDto(location);
        else return null;
    }
}
