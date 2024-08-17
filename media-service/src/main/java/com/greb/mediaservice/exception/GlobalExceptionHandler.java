package com.greb.mediaservice.exception;

import com.greb.mediaservice.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger LOGGER= LoggerFactory.getLogger(Exception.class);

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorDto> handleBadRequestException(Exception ex){
        LOGGER.error(ex.getMessage());
        var errorDto=new ErrorDto(HttpStatus.BAD_REQUEST,"Bad request", ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<ErrorDto> handleUnknownException(FileException ex){
        LOGGER.error("File Exception",ex.getMessage());
        var errorDto= new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "File exception",ex.getMessage());
        return ResponseEntity.status(500).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleUnknownException(Exception ex){
        LOGGER.error(ex.getMessage());
        var errorDto= new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error",ex.getMessage());
        return ResponseEntity.status(500).body(errorDto);
    }
}
