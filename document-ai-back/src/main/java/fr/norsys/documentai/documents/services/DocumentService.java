package fr.norsys.documentai.documents.services;

import fr.norsys.documentai.documents.dtos.DocumentResponse;
import fr.norsys.documentai.documents.dtos.UpdateDocumentRequest;
import fr.norsys.documentai.documents.entities.Document;
import fr.norsys.documentai.documents.enums.ComparatorOperator;
import fr.norsys.documentai.documents.exceptions.DocumentNotFoundException;
import fr.norsys.documentai.documents.exceptions.InvalidComparatorOperatorException;
import fr.norsys.documentai.documents.repositories.DocumentRepository;
import fr.norsys.documentai.documents.specs.DocumentDateSpecs;
import fr.norsys.documentai.documents.specs.DocumentFileSizeSpecs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final MessageSource messageSource;

    public Page<DocumentResponse> getDocuments(
            Pageable pageable,
            String fileSizeOperatorStr,
            Integer fileSize,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate updatedAtStart,
            LocalDate updatedAtEnd
    ) {
        List<Specification<Document>> documentSpecs = new ArrayList<>();

        if (fileSize != null && fileSizeOperatorStr != null && !fileSizeOperatorStr.isEmpty()) {
            ComparatorOperator operator;
            try {
                operator = ComparatorOperator.valueOf(fileSizeOperatorStr);
           } catch (IllegalArgumentException ex) {
                throw new InvalidComparatorOperatorException(
                    messageSource.getMessage("invalid.filesize.operator.error", null, Locale.getDefault())
                );
           }

            Specification<Document> fileSizeSpec = switch (operator) {
                case GREATER_THAN -> DocumentFileSizeSpecs.fileSizeGreaterThan(fileSize);
                case GREATER_THAN_OR_EQUAL -> DocumentFileSizeSpecs.fileSizeGreaterThanOrEqual(fileSize);
                case LESS_THAN -> DocumentFileSizeSpecs.fileSizeLessThan(fileSize);
                case LESS_THAN_OR_EQUAL -> DocumentFileSizeSpecs.fileSizeLessThanOrEqual(fileSize);
                case EQUAL -> DocumentFileSizeSpecs.fileSizeEqual(fileSize);
            };
            documentSpecs.add(fileSizeSpec);
        }

        if (createdAtStart != null && createdAtEnd != null) {
            documentSpecs.add(DocumentDateSpecs.createdAtBetween(createdAtStart, createdAtEnd));
        }

        if (updatedAtStart != null && updatedAtEnd != null) {
            documentSpecs.add(DocumentDateSpecs.updatedAtBetween(updatedAtStart, updatedAtEnd));
        }

        Specification<Document> finalSpec = documentSpecs.stream()
                .reduce(Specification::and)
                .orElse(null);

        return documentRepository.findAll(finalSpec, pageable)
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
