package com.greb.userservice.dtos.Driver;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDate;

@Data
public class RegisterDriverDto {
    @NotBlank
    private String name;

    private LocalDate dateOfBirth;

    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}", message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank
    private String address;

    @NotBlank
    private String licenceNumber;

    @NotBlank
    private String identityCard;

    @UUID
    private String avatarId;
}
