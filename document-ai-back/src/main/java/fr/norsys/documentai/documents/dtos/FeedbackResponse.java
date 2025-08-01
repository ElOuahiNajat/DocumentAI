package fr.norsys.documentai.documents.dtos;

import java.time.LocalDateTime;

public record FeedbackResponse(
    String content,
    Short note,
    LocalDateTime createdAt
) {
}