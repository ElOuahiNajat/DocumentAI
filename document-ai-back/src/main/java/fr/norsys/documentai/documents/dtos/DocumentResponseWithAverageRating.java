package fr.norsys.documentai.documents.dtos;

import java.util.List;

public record DocumentResponseWithAverageRating(
        DocumentResponse document,
        double averageRating
) {}