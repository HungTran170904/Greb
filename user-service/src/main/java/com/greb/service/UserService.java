package com.greb.service;

import com.greb.dto.User.ResponseUserDto;
import com.greb.dto.User.UserConverter;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RealmResource realmResource;
    private final UserConverter userConverter;

    public ResponseUserDto getProfile(String userId){
        var user= realmResource.users().get(userId).toRepresentation();
        return userConverter.toDto(user);
    }
}
