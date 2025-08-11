package fr.norsys.documentai.documents.dtos;

import fr.norsys.documentai.documents.entities.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        String title,
        String author,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String fileType,
        Integer fileSize,
        List<FeedbackResponse> feedbacks,
        Double averageRating 
) {
    public DocumentResponse(Document doc, List<FeedbackResponse> feedbacks, Double averageRating) {
        this(
                doc.getId(),
                doc.getTitle(),
                doc.getAuthor(),
                doc.getDescription(),
                doc.getCreatedAt(),
                doc.getUpdatedAt(),
                doc.getFileType(),
                doc.getFileSize(),
                feedbacks,
                averageRating
        );
    }
}