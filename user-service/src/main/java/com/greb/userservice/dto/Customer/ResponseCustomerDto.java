package com.greb.dto.Customer;

import com.greb.dto.User.ResponseUserDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseCustomerDto {
    private String id;

    private String name;

    private String address;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private Boolean gender;

    private Integer discountPoint;

    private String userId;

    private String avatarId;

    private ResponseUserDto user;
}
