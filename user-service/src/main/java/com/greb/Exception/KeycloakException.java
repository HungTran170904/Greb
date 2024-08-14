package com.greb.Exception;
import lombok.Data;

import javax.ws.rs.core.Response;

@Data
public class KeycloakException extends RuntimeException{
    private Response response;

    public KeycloakException(Response response){
        this.response=response;
    }
}
