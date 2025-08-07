package fr.norsys.documentai.users.services;

import fr.norsys.documentai.users.dtos.CreateUserRequest;
import fr.norsys.documentai.users.dtos.UserResponse;
import fr.norsys.documentai.users.entities.User;
import fr.norsys.documentai.users.exceptions.UserAlreadyExistException;
import fr.norsys.documentai.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

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
}
