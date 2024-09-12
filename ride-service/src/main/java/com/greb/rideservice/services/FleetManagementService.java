package com.greb.rideservice.services;

import com.greb.rideservice.configs.ServiceUrlConfig;
import com.greb.rideservice.dtos.ServiceType.ResServiceTypeDto;
import com.greb.rideservice.dtos.Vehicle.ResponseVehicleDto;
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
public class FleetManagementService extends AbstractCircuitBreakFallbackHandler{
    private final ServiceUrlConfig serviceUrlConfig;
    private final RestClient restClient;

    public ResponseVehicleDto getVehicleById(String vehicleId) {
        final String jwt = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();
        URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.fleetManagementServiceUrl())
                .path("/vehicles/"+vehicleId)
                .build().toUri();
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(ResponseVehicleDto.class);
    }

    public ResServiceTypeDto getServiceTypeById(String serviceTypeId) {
        final String jwt = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();
        URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.fleetManagementServiceUrl())
                .path("/service-types/"+serviceTypeId)
                .build().toUri();
        return restClient.get()
                .uri(uri)
                .headers(h->h.setBearerAuth(jwt))
                .retrieve()
                .body(ResServiceTypeDto.class);
    }

    public List<ResServiceTypeDto> getAllServiceTypes() {
        final String jwt = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();
        URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.fleetManagementServiceUrl())
                .path("/service-types")
                .build().toUri();
        return restClient.get()
                .uri(uri)
                .headers(h->h.setBearerAuth(jwt))
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }
}
