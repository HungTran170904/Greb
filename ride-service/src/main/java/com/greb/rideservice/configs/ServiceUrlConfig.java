package com.greb.rideservice.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "greb.services")
public record ServiceUrlConfig (
        String userServiceUrl,
        String notificationServiceUrl,
        String locationServiceUrl,
        String fleetManagementServiceUrl,
        String websocketServiceUrl
){}
