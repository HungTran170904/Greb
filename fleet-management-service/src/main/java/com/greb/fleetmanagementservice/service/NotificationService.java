package com.greb.fleetmanagementservice.service;

import com.greb.fleetmanagementservice.config.ServiceUrlConfig;
import com.greb.fleetmanagementservice.dto.Notification.MailDto;
import com.greb.fleetmanagementservice.dto.Notification.NoticeDto;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class NotificationService extends AbstractCircuitBreakFallbackHandler{
    private final RestClient restClient;
    private final ServiceUrlConfig serviceUrlConfig;
    private final RealmResource realmResource;
    private final UserService userService;

    public void sendNotificationToAdmins(NoticeDto dto){
        final String jwt=  ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();
        CompletableFuture.runAsync(()->{
            RoleResource roleResource = realmResource.roles().get("ADMIN");
            List<String> fcmTokens = new ArrayList();
            roleResource.getRoleUserMembers().forEach(user -> {
                if(user.getAttributes().containsKey("fcmToken"))
                    fcmTokens.add(user.getAttributes().get("fcmToken").get(0));
            });
            if(fcmTokens!=null&&!fcmTokens.isEmpty()){
                dto.setRegistrationTokens(fcmTokens);
                sendWebPush(dto, jwt);
            }
        });
    }

    public void sendEmailToDriver(String driverId,String content){
        final String jwt=  ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getTokenValue();
        CompletableFuture.runAsync(()->{
            var userId= userService.getUserIdOfDriver(driverId, jwt);
            var user= realmResource.users().get(userId).toRepresentation();
            var mailDto= MailDto.builder()
                    .subject("Vehicle Request")
                    .destEmails(List.of(user.getEmail()))
                    .content(content)
                    .build();
            sendEmail(mailDto, jwt);
        });
    }

    public void sendWebPush(NoticeDto dto, String jwt){
        final URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.notificationServiceUrl())
                        .path("/webpush")
                        .build().toUri();
        restClient.post()
                .uri(uri)
                .headers(h->h.setBearerAuth(jwt))
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    public void sendEmail(MailDto dto, String jwt){
        final URI uri= UriComponentsBuilder.fromHttpUrl(serviceUrlConfig.notificationServiceUrl())
                .path("/email")
                .build().toUri();
        restClient.post()
                .uri(uri)
                .headers(h->h.setBearerAuth(jwt))
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }
}
