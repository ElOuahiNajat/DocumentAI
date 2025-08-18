package fr.norsys.documentai.authentication.repositories;

import fr.norsys.documentai.authentication.entities.RefreshToken;
import fr.norsys.documentai.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
