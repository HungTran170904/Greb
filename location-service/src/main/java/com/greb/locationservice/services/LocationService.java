package com.greb.locationservice.services;

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

    public Location searchBestVehicles(
            Double userLon,
            Double userLat,
            String serviceId,
            Double maxDistance,
            String rejectedDriverIdsStr) {
        Query query = new Query();

        var rejectedDriverIds= Arrays.stream(rejectedDriverIdsStr.split("\\,")).toList();
        query.addCriteria(Criteria.where("driverId").not().in(rejectedDriverIds));

        GeoJsonPoint position = new GeoJsonPoint(userLon, userLat);
        query.addCriteria(Criteria.where("currPosition")
                .near(position).maxDistance(maxDistance*1000));

        query.addCriteria(Criteria.where("serviceId").is(serviceId));

        query.addCriteria(Criteria.where("rideId").exists(false)
                .orOperator(Criteria.where("rideId").isNull()));

        var result= mongoTemplate.find(query, Location.class);
        if(result.size()>0) return result.get(0);
        else return null;
    }

    public List<Location> searchNearVehicles(
            Double choosedLon,
            Double choosedLat,
            String serviceId,
            Double maxDistance,
            Boolean isOnline
    ){
        Query query = new Query();
        if(choosedLon != null&&choosedLat!=null&&maxDistance!=null){
            GeoJsonPoint position = new GeoJsonPoint(choosedLon, choosedLat);
            query.addCriteria(Criteria.where("currPosition")
                    .near(position).maxDistance(maxDistance*1000));
        }
        if(serviceId!=null){
            query.addCriteria(Criteria.where("serviceId").is(serviceId));
        }
        if(isOnline==true)
            query.addCriteria(Criteria.where("rideId").exists(true));
        else query.addCriteria(Criteria.where("rideId").not().exists(true));
        return mongoTemplate.find(query, Location.class);
    }

    public List<Location> findByVehicleIds(List<String> vehicleIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(vehicleIds));
        return mongoTemplate.find(query, Location.class);
    }
}
