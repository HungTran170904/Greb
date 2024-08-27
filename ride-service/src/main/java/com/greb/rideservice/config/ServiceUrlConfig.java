package com.greb.rideservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "greb.services")
public record ServiceUrlConfig (
        String userServiceUrl,
        String notificationServiceUrl
){}
