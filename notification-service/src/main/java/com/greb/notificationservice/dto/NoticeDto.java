package com.greb.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class NoticeDto {
    @NotBlank
    private String title;

    @NotEmpty
    private String content;

    private String image;

    private Map<String, String> data;

    @NotNull
    private List<String> registrationTokens;
}
