package com.greb.rideservice.services;

import com.greb.rideservice.configs.ServiceUrlConfig;
import com.greb.rideservice.dtos.User.JobStatus;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class UserService extends AbstractCircuitBreakFallbackHandler{
    private final RestClient restClient;
    private final ServiceUrlConfig serviceUrlConfig;

    @Retry(name="restApi")
    @CircuitBreaker(name="restCircuitBreaker", fallbackMethod = "handleFallback")
    public String getCustomerId(){
        String jwt= ((Jwt)SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();
        URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.userServiceUrl())
                .path("/private/customers/id")
                .build().toUri();
        return restClient.get()
                .uri(uri)
                .headers(h->h.setBearerAuth(jwt))
                .retrieve()
                .body(String.class);
    }

    @Retry(name="restApi")
    @CircuitBreaker(name="restCircuitBreaker", fallbackMethod="handleFallback")
    public String getDriverId(){
        final String jwt = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();
        final URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.userServiceUrl())
                .path("/private/drivers/id")
                .build().toUri();
        return restClient.get()
                .uri(uri)
                .headers(h->h.setBearerAuth(jwt))
                .retrieve()
                .body(String.class);
    }
}
