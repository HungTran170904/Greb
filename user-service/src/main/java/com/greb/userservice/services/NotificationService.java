package com.greb.userservice.services;

import com.greb.userservice.configs.ServiceUrlConfig;
import com.greb.userservice.dtos.Notification.MailDto;
import com.greb.userservice.dtos.Notification.NoticeDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class NotificationService extends AbstractCircuitBreakFallbackHandler{
    private final ServiceUrlConfig serviceUrlConfig;
    private final RestClient restClient;


    @Retry(name="restApi")
    @CircuitBreaker(name="restCircuitBreaker", fallbackMethod="handleBodilessFallback")
    public void sendWebPush(NoticeDto dto, String jwt){
        final URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.notificationServiceUrl())
                .path("/send-webpush")
                .build().toUri();
        restClient.post()
                .uri(uri)
                .headers(h->h.setBearerAuth(jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    @Retry(name="restApi")
    @CircuitBreaker(name="restCircuitBreaker", fallbackMethod="handleBodilessFallback")
    public void sendEmail(MailDto dto, String jwt){
        final URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.notificationServiceUrl())
                .path("/send-email")
                .build().toUri();
        restClient.post()
                .uri(uri)
                .headers(h->h.setBearerAuth(jwt))
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }
}
