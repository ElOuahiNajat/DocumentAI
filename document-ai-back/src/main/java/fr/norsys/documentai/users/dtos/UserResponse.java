package fr.norsys.documentai.users.dtos;

import fr.norsys.documentai.users.enums.Role;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Role role
) {
}
