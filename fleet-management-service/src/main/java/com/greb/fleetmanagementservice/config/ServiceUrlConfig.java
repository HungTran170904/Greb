package com.greb.fleetmanagementservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "greb.services")
public record ServiceUrlConfig (
        String userServiceUrl
){}
