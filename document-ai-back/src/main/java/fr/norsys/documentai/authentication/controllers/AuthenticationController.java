package fr.norsys.documentai.authentication.controllers;

import fr.norsys.documentai.users.entities.User;
import fr.norsys.documentai.authentication.services.AuthenticationService;
import fr.norsys.documentai.authentication.dto.AuthenticationRequest;
import fr.norsys.documentai.authentication.dto.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
