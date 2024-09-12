package com.greb.notificationservice.controller;

import com.greb.notificationservice.dto.MailDto;
import com.greb.notificationservice.dto.NoticeDto;
import com.greb.notificationservice.service.MailService;
import com.greb.notificationservice.service.WebPushService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final WebPushService webPushService;
    private final MailService mailService;

    @PostMapping("/webpush")
    public ResponseEntity<Void> sendWebPushNotification(
            @RequestBody @Valid NoticeDto noticeDto
    ){
        webPushService.sendNotification(noticeDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(
            @RequestBody @Valid MailDto mailDto
    ){
        mailService.sendMailToMultipleDests(mailDto);
        return ResponseEntity.noContent().build();
    }
}
