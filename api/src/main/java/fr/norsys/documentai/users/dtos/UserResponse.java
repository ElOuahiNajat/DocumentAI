package fr.norsys.documentai.users.dtos;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email
) {
}
