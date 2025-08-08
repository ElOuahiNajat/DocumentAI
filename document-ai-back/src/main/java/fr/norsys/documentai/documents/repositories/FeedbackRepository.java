package fr.norsys.documentai.documents.repositories;

import fr.norsys.documentai.documents.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findByDocumentIdOrderByCreatedAtDesc(UUID documentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Feedback f WHERE f.document.id = :documentId")
    void deleteByDocumentId(@Param("documentId") UUID documentId);
}
