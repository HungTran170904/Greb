package com.greb.mediaservice.dto;

import com.greb.mediaservice.model.Media;
import org.springframework.core.io.Resource;

public record ResponseFileDto (
    Resource resource,
    Media media
){}
