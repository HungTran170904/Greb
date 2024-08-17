package com.greb.mediaservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="file")
public record FileStorageConfig(String uploadPath) {
}
