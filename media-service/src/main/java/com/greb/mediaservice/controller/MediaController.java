package com.greb.mediaservice.controller;

import com.greb.mediaservice.model.Media;
import com.greb.mediaservice.service.MediaService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MediaController {
    public final MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<Media> uploadFile(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam(value = "deletedMediaId",defaultValue = "", required = false)
                String deletedMediaId
    ){
        var resource=mediaService.uploadAndDelete(multipartFile, deletedMediaId);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/download/{mediaId}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("mediaId") @UUID String mediaId,
            HttpServletResponse response
    ){
        var dto= mediaService.downloadFile(mediaId, response);
        var media=dto.media();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(media.getFileType()))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\""+media.getFileName()+"\""
                )
                .body(dto.resource());
    }

    @DeleteMapping("/delete/{mediaId}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable("mediaId") @UUID String mediaId
    ){
        mediaService.deleteFile(mediaId);
        return ResponseEntity.noContent().build();
    }
}
