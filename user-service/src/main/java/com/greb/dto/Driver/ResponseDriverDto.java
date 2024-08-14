package com.greb.dto.Driver;

import com.greb.dto.User.ResponseUserDto;
import com.greb.model.enums.JobStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseDriverDto {
    private String id;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    private String address;

    private String licenceNumber;

    private String identityCard;

    private JobStatus jobStatus;

    private String userId;

    private String avatarId;

    private ResponseUserDto user;
}
