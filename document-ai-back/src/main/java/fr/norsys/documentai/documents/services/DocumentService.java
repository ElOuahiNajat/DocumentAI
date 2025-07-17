package fr.norsys.documentai.documents.services;

import fr.norsys.documentai.documents.dtos.DocumentResponse;
import fr.norsys.documentai.documents.dtos.UpdateDocumentRequest;
import fr.norsys.documentai.documents.entities.Document;
import fr.norsys.documentai.documents.exceptions.DocumentNotFoundException;
import fr.norsys.documentai.documents.repositories.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final MessageSource messageSource;

    public Page<DocumentResponse> getDocumentsPaginated(Pageable pageable) {
        Page<Document> documentsPage = documentRepository.findAll(pageable);

        return documentsPage.map(DocumentResponse::new);
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
