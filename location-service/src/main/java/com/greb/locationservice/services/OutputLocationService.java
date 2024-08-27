package com.greb.locationservice.services;

import com.greb.locationservice.models.OutputLocation;
import com.greb.locationservice.models.Position;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutputLocationService {
    private final MongoTemplate mongoTemplate;

    public List<OutputLocation> searchBestVehicles(
            Double userLon,
            Double userLat,
            String serviceId,
            Double maxDistance,
            Integer limit) {
        Query query = new Query();
        GeoJsonPoint position = new GeoJsonPoint(userLon, userLat);
        query.addCriteria(Criteria.where("currPosition")
                .near(position).maxDistance(maxDistance));
        query.addCriteria(Criteria.where("serviceId").is(serviceId));
        query.limit(limit);
        return mongoTemplate.find(query, OutputLocation.class);
    }

    public List<OutputLocation> searchNearVehicles(
            Double choosedLon,
            Double choosedLat,
            String serviceId,
            Double maxDistance,
            Integer limit
    ){
        Query query = new Query();
        if(choosedLon != null&&choosedLat!=null&&maxDistance!=null){
            GeoJsonPoint position = new GeoJsonPoint(choosedLon, choosedLat);
            query.addCriteria(Criteria.where("currPosition")
                    .near(position).maxDistance(maxDistance));
        }
        if(serviceId!=null){
            query.addCriteria(Criteria.where("serviceId").is(serviceId));
        }
        query.limit(limit);
        return mongoTemplate.find(query, OutputLocation.class);
    }

    public List<OutputLocation> findByVehicleIds(List<String> vehicleIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(vehicleIds));
        return mongoTemplate.find(query, OutputLocation.class);
    }


}
