package com.greb.userservice.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="greb.services")
public record ServiceUrlConfig(
        String notificationServiceUrl
) {
}
