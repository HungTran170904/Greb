package com.greb.notificationservice.exception;

import com.greb.notificationservice.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger LOGGER= LoggerFactory.getLogger(Exception.class);

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorDto> handleBadRequestException(Exception ex){
        LOGGER.error(ex.getMessage());
        var errorDto=new ErrorDto(HttpStatus.BAD_REQUEST,"Bad request", ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorDto> handleValidationException(BindException ex){
        LOGGER.error(ex.getMessage());
        Map<String, String> errors = new HashMap();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        var errorDto= new ErrorDto(HttpStatus.BAD_REQUEST, "Validation error","", errors);
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleUnknownException(Exception ex){
        LOGGER.error(ex.getMessage());
        var errorDto= new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error",ex.getMessage());
        return ResponseEntity.status(500).body(errorDto);
    }
}
