package com.greb.userservice.dtos.User;

import lombok.Data;

@Data
public class ResponseUserDto {
    private String username;

    private String email;

    private String firstName;

    private String lastName;
}
