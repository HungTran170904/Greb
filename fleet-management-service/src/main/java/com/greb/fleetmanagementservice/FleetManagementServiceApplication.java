package com.greb.fleetmanagementservice;

import com.greb.fleetmanagementservice.config.ServiceUrlConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ServiceUrlConfig.class)
public class FleetManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FleetManagementServiceApplication.class, args);
    }

}
