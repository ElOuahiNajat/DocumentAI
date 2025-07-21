package fr.norsys.documentai.documents.services;

import fr.norsys.documentai.documents.dtos.DocumentResponse;
import fr.norsys.documentai.documents.dtos.UpdateDocumentRequest;
import fr.norsys.documentai.documents.entities.Document;
import fr.norsys.documentai.documents.exceptions.DocumentNotFoundException;
import fr.norsys.documentai.documents.repositories.DocumentRepository;
import fr.norsys.documentai.documents.specs.DocumentDateSpecs;
import fr.norsys.documentai.documents.specs.DocumentFileSizeSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Locale;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final MessageSource messageSource;

    public Page<DocumentResponse> getDocuments(
            Pageable pageable,
            String fileSizeOperator,
            Long fileSize,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate updatedAtStart,
            LocalDate updatedAtEnd
    ) {
        Specification<Document> spec = Specification.where(null);

        
        if (fileSize != null && fileSizeOperator != null) {
            spec = spec.and(switch (fileSizeOperator) {
                case ">" -> DocumentFileSizeSpecs.fileSizeGreaterThan(fileSize);
                case ">=" -> DocumentFileSizeSpecs.fileSizeGreaterThanOrEqual(fileSize);
                case "<" -> DocumentFileSizeSpecs.fileSizeLessThan(fileSize);
                case "<=" -> DocumentFileSizeSpecs.fileSizeLessThanOrEqual(fileSize);
                case "=" -> DocumentFileSizeSpecs.fileSizeEqual(fileSize);
                default -> throw new IllegalArgumentException("Invalid file size operator: " + fileSizeOperator);
            });
        }

        
        if (createdAtStart != null && createdAtEnd != null) {
            spec = spec.and(DocumentDateSpecs.createdAtBetween(createdAtStart, createdAtEnd));
        }

        
        if (updatedAtStart != null && updatedAtEnd != null) {
            spec = spec.and(DocumentDateSpecs.updatedAtBetween(updatedAtStart, updatedAtEnd));
        }

        return documentRepository.findAll(spec, pageable)
                .map(DocumentResponse::new);
    }


    
    public void updateDocument(UUID id, UpdateDocumentRequest request) throws DocumentNotFoundException {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(
                        messageSource.getMessage("document.not.found.error", null, Locale.getDefault())
                ));

        doc.setTitle(request.title());
        doc.setAuthor(request.author());
        doc.setDescription(request.description());
        documentRepository.save(doc);
    }
   
    

}
