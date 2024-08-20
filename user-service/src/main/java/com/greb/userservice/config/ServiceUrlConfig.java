package com.greb.userservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="greb.services")
public record ServiceUrlConfig(
        String notificationServiceUrl
) {
}
