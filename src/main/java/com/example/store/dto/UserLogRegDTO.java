package com.example.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Uses for registration and login.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogRegDTO {
        @Email
        @NotBlank
        private String email;
        @NotBlank
        private String password;
}
