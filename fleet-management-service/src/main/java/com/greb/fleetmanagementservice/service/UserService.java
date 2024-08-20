package com.greb.fleetmanagementservice.service;

import com.greb.fleetmanagementservice.config.ServiceUrlConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestClient restClient;
    private final ServiceUrlConfig serviceUrlConfig;

    public String getDriverId(){
        final String jwt = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();
        final URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.userServiceUrl())
                .path("/driver/private/driver-id")
                .build().toUri();
        return restClient.get()
                .uri(uri)
                .headers(h->h.setBearerAuth(jwt))
                .retrieve()
                .body(String.class);
    }

    public String getUserIdOfDriver(String driverId, String jwt ){
        final URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.userServiceUrl())
                .path("/driver/private/user-id")
                .queryParam("driverId", driverId)
                .build().toUri();
        return restClient.get()
                .uri(uri)
                .headers(h->h.setBearerAuth(jwt))
                .retrieve()
                .body(String.class);
    }
}
