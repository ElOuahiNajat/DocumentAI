package fr.norsys.documentai.authentication.services;

import fr.norsys.documentai.users.entities.User;
import fr.norsys.documentai.users.repositories.UserRepository;
import fr.norsys.documentai.users.exceptions.UserNotFoundException;
import fr.norsys.documentai.authentication.dto.AuthenticationRequest;
import fr.norsys.documentai.authentication.dto.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.context.MessageSource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MessageSource messageSource;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("user.not.found.error", null, Locale.getDefault())));


        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("firstName", user.getFirstName());
        extraClaims.put("lastName", user.getLastName());
        String accessToken = jwtService.generateToken(extraClaims, user.getEmail());

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
