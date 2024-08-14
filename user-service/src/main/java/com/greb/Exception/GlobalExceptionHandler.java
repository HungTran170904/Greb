package com.greb.Exception;

import com.greb.dto.ErrorDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final Logger LOGGER= LoggerFactory.getLogger(Exception.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequestException(Exception ex){
        LOGGER.error(ex.getMessage());
        var errorDto=new ErrorDto(HttpStatus.BAD_REQUEST,"Bad request", ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorDto> handleUnAuthorizedException(Exception ex){
        LOGGER.error(ex.getMessage());
        var errorDto=new ErrorDto(HttpStatus.UNAUTHORIZED,"Unauthorized", ex.getMessage());
        return ResponseEntity.status(403).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(MethodArgumentNotValidException ex){
        LOGGER.error(ex.getMessage());
        Map<String, String> errors = new HashMap();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        var errorDto= new ErrorDto(HttpStatus.BAD_REQUEST, "Validation error","", errors);
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<ErrorDto> handleKeyCloakException(KeycloakException ex){
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        ex.getResponse().readEntity(InputStream.class))
                )
        ) {
            StringBuilder errorDetails = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                errorDetails.append(line);
            }
            ErrorDto errorDto= new ErrorDto(HttpStatus.BAD_REQUEST,"keycloak exception", errorDetails.toString());
            return ResponseEntity.badRequest().body(errorDto);
        } catch (Exception unkownError) {
            var errorDto= new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error",unkownError.getMessage());
            return ResponseEntity.status(500).body(errorDto);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleUnknownException(Exception ex){
        LOGGER.error(ex.getMessage());
        var errorDto= new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error",ex.getMessage());
        return ResponseEntity.status(500).body(errorDto);
    }
}
