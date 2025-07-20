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
        Integer fileSize,
        String formattedFileSize
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
                doc.getFileSize(),
                formatFileSize(doc.getFileSize())
        );
    }

    private static String formatFileSize(Integer sizeKo) {
        if (sizeKo == null) return "0 Ko";
        double sizeMo = sizeKo / 1024.0;
        if (sizeMo >= 1) {
            return String.format("%.2f Mo", sizeMo);
        } else {
            return sizeKo + " Ko";
        }
    }
}
