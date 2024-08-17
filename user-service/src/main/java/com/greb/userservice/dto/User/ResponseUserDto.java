package com.greb.dto.User;

import lombok.Data;

import java.util.List;

@Data
public class ResponseUserDto {
    private String username;

    private String email;

    private String firstName;

    private String lastName;
}
