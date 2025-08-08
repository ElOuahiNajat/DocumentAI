package fr.norsys.documentai.users.services;

import fr.norsys.documentai.users.dtos.CreateUserRequest;
import fr.norsys.documentai.users.dtos.UserResponse;
import fr.norsys.documentai.users.dtos.UpdateUserRequest;
import fr.norsys.documentai.users.entities.User;
import fr.norsys.documentai.users.exceptions.UserAlreadyExistException;
import fr.norsys.documentai.users.exceptions.UserNotFoundException;
import fr.norsys.documentai.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import java.io.ByteArrayOutputStream;
import org.springframework.data.domain.Page;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;

    public Page<UserResponse> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
        .map(user -> new UserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getRole()
        ));
    }

    public void createUser(CreateUserRequest request) {
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        userRepository.save(user);
    }
    public ByteArrayInputStream exportUsersToCSV() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter osWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(osWriter)) {

            writer.println("FirstName;LastName;Email;Role");

            int chunkSize = 500;
            int page = 0;
            Page<User> userPage;

            do {
                userPage = userRepository.findAll(
                        PageRequest.of(
                                page,
                                chunkSize,
                                Sort.by(Sort.Direction.ASC, "firstName") 
                        )
                );

                for (User user : userPage.getContent()) {
                    writer.printf("%s;%s;%s;%s%n",
                            escapeCsv(user.getFirstName()),
                            escapeCsv(user.getLastName()),
                            escapeCsv(user.getEmail()),
                            escapeCsv(user.getRole())
                    );
                }

                page++;
            } while (userPage.hasNext());

            writer.flush();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("export.csv.failed", null, Locale.getDefault());
            throw new RuntimeException(errorMessage, e);
        }
    }

    private String escapeCsv(Object value) {
        if (value == null) return "";
        String str = value.toString();
        if (str.contains(";") || str.contains("\"") || str.contains("\n")) {
            str = str.replace("\"", "\"\"");
            return "\"" + str + "\"";
        }
        return str;
    }


    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("user.not.found.error", null, Locale.getDefault())));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setRole(request.role());

        User updatedUser = userRepository.save(user);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getEmail(),
                updatedUser.getRole()
        );
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
