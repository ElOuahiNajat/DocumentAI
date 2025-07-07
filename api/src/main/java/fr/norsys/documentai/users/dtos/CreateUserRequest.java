package fr.norsys.documentai.users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @Email
        @NotBlank
        String email
) {
}
