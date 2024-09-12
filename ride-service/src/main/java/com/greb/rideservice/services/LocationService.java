package com.greb.rideservice.services;

import com.greb.rideservice.configs.ServiceUrlConfig;
import com.greb.rideservice.dtos.Location.LocationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final RestClient restClient;
    private final ServiceUrlConfig serviceUrlConfig;

    public LocationDto searchBestLocation(
            Double userLon,
            Double userLat,
            String serviceTypeId,
            Double maxDistance,
            List<String> rejectedDriverIds
    ){
        String rejectedDriverIdsStr= "";
        if(rejectedDriverIds!=null)
            rejectedDriverIdsStr=rejectedDriverIds.toString()
                                .replace("[", "")
                                .replace("]", "");

        URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.locationServiceUrl())
                .path("/private/locations/best-location")
                .queryParam("userLon", userLon)
                .queryParam("userLat", userLat)
                .queryParam("serviceTypeId", serviceTypeId)
                .queryParam("maxDistance", maxDistance)
                .queryParam("rejectedDriverIds", rejectedDriverIdsStr)
                .build().toUri();
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(LocationDto.class);
    }

    public LocationDto getByDriverId(String driverId){
        final String jwt = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();
        URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.locationServiceUrl())
                .path("/drivers/"+driverId+"/location")
                .build().toUri();
        return restClient.get()
                .uri(uri)
                .headers(h->h.setBearerAuth(jwt))
                .retrieve()
                .body(LocationDto.class);
    }
}
