package fr.norsys.documentai.users.controllers;

import fr.norsys.documentai.exceptions.MethodArgumentNotValidExceptionHandler;
import fr.norsys.documentai.users.dtos.CreateUserRequest;
import fr.norsys.documentai.users.dtos.UserResponse;
import fr.norsys.documentai.users.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements MethodArgumentNotValidExceptionHandler {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> users = userService.getUsers();

        return ResponseEntity.ok().body(users);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        userService.createUser(createUserRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/export/csv")
    public ResponseEntity<InputStreamResource> exportUsersToCSV() {
        InputStreamResource resource = new InputStreamResource(userService.exportUsersToCSV());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

}
