package com.greb.userservice.dto.Notification;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MailDto {
    private List<String> destEmails;

    private String content;

    private String subject;
}
