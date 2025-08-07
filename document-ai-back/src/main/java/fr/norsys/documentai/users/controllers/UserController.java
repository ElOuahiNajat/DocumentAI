package fr.norsys.documentai.users.controllers;

import fr.norsys.documentai.exceptions.MethodArgumentNotValidExceptionHandler;
import fr.norsys.documentai.users.dtos.CreateUserRequest;
import fr.norsys.documentai.users.dtos.UserResponse;
import fr.norsys.documentai.users.dtos.UpdateUserRequest;
import fr.norsys.documentai.users.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements MethodArgumentNotValidExceptionHandler {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        Page<UserResponse> usersPage = userService.getUsers(pageable);
        return ResponseEntity.ok().body(usersPage);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserRequest request) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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


    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        UserResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }
}
