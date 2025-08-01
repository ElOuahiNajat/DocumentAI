package fr.norsys.documentai.documents.repositories;

import fr.norsys.documentai.documents.entities.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, UUID> {
    List<Evaluation> findByDocumentId(UUID documentId);
}
