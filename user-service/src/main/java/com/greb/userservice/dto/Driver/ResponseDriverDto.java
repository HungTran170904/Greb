package com.greb.userservice.dto.Driver;

import com.greb.userservice.dto.User.ResponseUserDto;
import com.greb.userservice.model.enums.JobStatus;
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

    private Integer totalRatings;

    private Integer totalRatingPoints;

    private ResponseUserDto user;
}
