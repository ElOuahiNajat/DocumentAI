package fr.norsys.documentai.documents.dtos;

import jakarta.validation.constraints.NotBlank;

public record UpdateDocumentRequest(
        @NotBlank()
        String title,
        @NotBlank()
        String author,
        @NotBlank()
        String description
) {}