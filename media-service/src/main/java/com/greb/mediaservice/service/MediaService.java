package com.greb.mediaservice.service;

import com.greb.mediaservice.config.FileStorageConfig;
import com.greb.mediaservice.dto.ResponseFileDto;
import com.greb.mediaservice.exception.BadRequestException;
import com.greb.mediaservice.exception.FileException;
import com.greb.mediaservice.model.Media;
import com.greb.mediaservice.repository.MediaRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

@Service
@Transactional
public class MediaService {
    private MediaRepository mediaRepo;
    private Path storageLocation;
    private Random rand;

    @Autowired
    public MediaService(MediaRepository mediaRepo, FileStorageConfig fileStorageConfig){
        this.mediaRepo=mediaRepo;
        this.storageLocation= Paths.get(fileStorageConfig.uploadPath()).toAbsolutePath().normalize();
        try{
            Files.createDirectories(this.storageLocation);
        }
        catch(IOException e){
            throw new FileException(e.getMessage());
        }
        rand = new Random();
    }

    @Transactional
    public Media uploadAndDelete(MultipartFile multipartFile, String deletedMediaId) {
        if(!multipartFile.getContentType().startsWith("image"))
            throw new BadRequestException("Only image file is allowed");

        if(!deletedMediaId.isEmpty()) deleteFile(deletedMediaId);

        int lastDotIndex=multipartFile.getOriginalFilename().lastIndexOf(".");
        var extension=multipartFile.getOriginalFilename().substring(lastDotIndex);
        var filename=multipartFile.getOriginalFilename().substring(0, lastDotIndex);
        String uniqueFilename=filename+"_"+rand.nextInt(1000)+extension;

        String targetPath=this.storageLocation.toString()+File.separator+uniqueFilename;
        File file=new File(targetPath);
        try{
            multipartFile.transferTo(file);
        }
        catch(Exception ex){
            throw new FileException(ex.getMessage());
        }
        Media media = Media.builder()
                .fileName(uniqueFilename)
                .fileSize(multipartFile.getSize())
                .filePath(targetPath)
                .fileType(multipartFile.getContentType())
                .build();
        return mediaRepo.save(media);
    }

    public void deleteFile(String mediaFileId){
        var media=mediaRepo.findById(mediaFileId).orElse(null);
        if(media==null) return;
        File file= new File(media.getFilePath());
        if(file.exists()) file.delete();
        mediaRepo.deleteById(mediaFileId);
    }

    public ResponseFileDto downloadFile(String mediaFileId, HttpServletResponse response) {
        var media = mediaRepo.findById(mediaFileId)
                .orElseThrow(() -> new BadRequestException("Media file not found"));
        File file = new File(media.getFilePath());
        if (!file.exists()) throw new FileException("Media file not found");
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\""+media.getFileName()+"\"");
        response.setContentType(media.getFileType());
        return new ResponseFileDto(new FileSystemResource(file), media);
    }
}
