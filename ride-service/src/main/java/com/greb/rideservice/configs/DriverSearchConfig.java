package com.greb.rideservice.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "driver-search")
public record DriverSearchConfig(
        Double maxDistance,
        Integer retryTime,
        Integer maxAcceptTime,
        Integer maxRound
){ }
