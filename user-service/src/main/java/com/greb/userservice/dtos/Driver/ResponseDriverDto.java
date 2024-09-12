package com.greb.userservice.dtos.Driver;

import com.greb.userservice.dtos.User.ResponseUserDto;
import com.greb.userservice.models.enums.JobStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseDriverDto {
    private String id;

    private String name;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    private String address;

    private String licenceNumber;

    private String identityCard;

    private JobStatus jobStatus;

    private String userId;

    private String avatarId;

    private Double averageRating;

    private ResponseUserDto user;
}
