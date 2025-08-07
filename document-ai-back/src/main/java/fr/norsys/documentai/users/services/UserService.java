package fr.norsys.documentai.users.services;

import fr.norsys.documentai.users.dtos.CreateUserRequest;
import fr.norsys.documentai.users.dtos.UserResponse;
import fr.norsys.documentai.users.entities.User;
import fr.norsys.documentai.users.exceptions.UserAlreadyExistException;
import fr.norsys.documentai.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.Sort;
import java.io.ByteArrayOutputStream;
import org.springframework.data.domain.Page;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map((user) -> new UserResponse(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole()
                ))
                .toList();
    }

    public void createUser(CreateUserRequest createUserRequest) {
        throw new UserAlreadyExistException(messageSource.getMessage("user.already.exists.error", null, Locale.getDefault()));
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

}
