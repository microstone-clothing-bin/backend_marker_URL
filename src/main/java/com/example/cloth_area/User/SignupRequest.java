package com.example.cloth_area.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @Email
    private String email;
}