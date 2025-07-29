package fr.norsys.documentai.documents.dtos;

import org.springframework.core.io.Resource;
import fr.norsys.documentai.documents.entities.Document;

public record DownloadedDocumentDTO(Document document, Resource resource) {
}
