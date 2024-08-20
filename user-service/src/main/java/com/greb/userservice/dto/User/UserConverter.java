package com.greb.userservice.dto.User;

import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserConverter {
    public ResponseUserDto toDto(UserRepresentation user){
        var dto = new ResponseUserDto();
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        return dto;
    }
}
