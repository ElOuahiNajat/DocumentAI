package fr.norsys.documentai.documents.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateDocumentRequest(
    @NotBlank
    String title,
    @NotBlank
    String author,
    @NotBlank
    String description,
    @NotNull
    MultipartFile file
) {}
