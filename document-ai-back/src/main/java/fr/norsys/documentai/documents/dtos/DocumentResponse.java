package fr.norsys.documentai.documents.dtos;

import fr.norsys.documentai.documents.entities.Document;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        String title,
        String author,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String fileType,
        Integer fileSize
        
) {
    public DocumentResponse(Document doc) {
        this(
                doc.getId(),
                doc.getTitle(),
                doc.getAuthor(),
                doc.getDescription(),
                doc.getCreatedAt(),
                doc.getUpdatedAt(),
                doc.getFileType(),
                doc.getFileSize()

        );
    }
   
}

