package fr.norsys.documentai.documents.repositories;

import fr.norsys.documentai.documents.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findByDocumentIdOrderByCreatedAtDesc(UUID documentId);
}
