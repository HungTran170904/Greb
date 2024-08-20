package com.greb.notificationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="firebase")
public record FirebaseProperties(
        String serviceAccountKey
) {}
