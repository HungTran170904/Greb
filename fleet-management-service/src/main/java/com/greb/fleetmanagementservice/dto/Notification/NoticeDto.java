package com.greb.fleetmanagementservice.dto.Notification;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class NoticeDto {
    private String title;

    private String content;

    private String image;

    private Map<String, String> data;

    private List<String> registrationTokens;
}
