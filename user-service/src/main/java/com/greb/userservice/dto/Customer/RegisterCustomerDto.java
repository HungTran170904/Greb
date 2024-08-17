package com.greb.dto.Customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDate;

@Data
public class RegisterCustomerDto {
    @NotBlank
    private String name;

    private String address;

    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}", message = "Invalid phone number")
    private String phoneNumber;

    private LocalDate dateOfBirth;

    private Boolean gender;

    @UUID
    private String avatarId;
}
