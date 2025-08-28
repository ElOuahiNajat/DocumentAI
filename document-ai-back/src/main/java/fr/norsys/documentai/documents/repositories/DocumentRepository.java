package fr.norsys.documentai.documents.repositories;

import fr.norsys.documentai.documents.entities.Document;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID>, JpaSpecificationExecutor<Document> {
    long countByFileType(String fileType);
    long countByCreatedAtAfter(LocalDateTime dateTime);
    @Query("SELECT COUNT(DISTINCT d) FROM Document d JOIN d.feedbacks f")
    long countDocumentsWithFeedback();
}
