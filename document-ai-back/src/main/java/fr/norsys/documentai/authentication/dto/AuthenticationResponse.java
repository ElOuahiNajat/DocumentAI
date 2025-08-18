package fr.norsys.documentai.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
}