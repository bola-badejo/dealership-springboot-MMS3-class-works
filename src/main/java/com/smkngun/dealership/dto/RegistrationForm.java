package com.smkngun.dealership.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// Form backing the /register page.
@Data
public class RegistrationForm {

    @NotBlank
    @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
    private String username;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank
    @Email(message = "Must be a valid email")
    private String email;

    @NotBlank
    private String fullName;

    private String phone;
    private String address;
    private String city;
}
