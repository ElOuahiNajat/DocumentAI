package fr.norsys.documentai.documents.repositories;

import fr.norsys.documentai.documents.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByDocumentId(UUID documentId);
}