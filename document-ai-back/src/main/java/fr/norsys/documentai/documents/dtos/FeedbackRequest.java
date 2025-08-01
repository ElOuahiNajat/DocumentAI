package fr.norsys.documentai.documents.dtos;

public record FeedbackRequest(
    String content,
    Short note    
) {
}
