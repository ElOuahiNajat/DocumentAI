package fr.norsys.documentai.documents.services;

import fr.norsys.documentai.documents.dtos.DocumentResponse;
import fr.norsys.documentai.documents.dtos.UpdateDocumentRequest;
import fr.norsys.documentai.documents.entities.Document;
import fr.norsys.documentai.documents.exceptions.DocumentNotFoundException;
import fr.norsys.documentai.documents.repositories.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Locale;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    public static final long TWO_MO_IN_KO = 2 * 1024L;
    private final DocumentRepository documentRepository;
    private final MessageSource messageSource;

    public Page<DocumentResponse> getDocuments(String filter, Pageable pageable) {
        if (filter == null || filter.isBlank() || filter.equalsIgnoreCase("none")) {
            return documentRepository.findAll(pageable)
                    .map(DocumentResponse::new);
        }

        filter = filter.toLowerCase();

        return switch (filter) {
            case "greaterthan2mo" -> getDocumentsGreaterThanTwoMo(pageable);
            case "createdtoday" -> getDocumentsCreatedToday(pageable);
            case "updatedbeforetoday" -> getDocumentsUpdatedBeforeToday(pageable);
            default -> {
                log.warn("Filtre inconnu demandé: {}", filter);
                throw new IllegalArgumentException("Filtre inconnu: " + filter);
            }
        };
    }

    private Page<DocumentResponse> getDocumentsGreaterThanTwoMo(Pageable pageable) {
        return documentRepository.findByFileSizeGreaterThan(TWO_MO_IN_KO, pageable)
                .map(DocumentResponse::new);
    }

    private Page<DocumentResponse> getDocumentsCreatedToday(Pageable pageable) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        return documentRepository.findByCreatedAtToday(start, end, pageable)
                .map(DocumentResponse::new);
    }

    private Page<DocumentResponse> getDocumentsUpdatedBeforeToday(Pageable pageable) {
        LocalDateTime before = LocalDate.now().atStartOfDay();
        return documentRepository.findByUpdatedAtBefore(before, pageable)
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
