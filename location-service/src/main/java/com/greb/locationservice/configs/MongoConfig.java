package com.greb.locationservice.configs;

import com.greb.locationservice.models.OutputLocation;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@RequiredArgsConstructor
public class MongoConfig {
    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initSchema(){
        if (!mongoTemplate.collectionExists(OutputLocation.class)) {
            mongoTemplate.createCollection(OutputLocation.class);
        }
    }
}
