package com.greb.mediaservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {
    @Id @UuidGenerator
    private String id;

    private String fileName;

    private String filePath;

    private Long fileSize;

    private String fileType;
}
