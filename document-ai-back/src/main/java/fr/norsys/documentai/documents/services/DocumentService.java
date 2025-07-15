package fr.norsys.documentai.documents.services;

import fr.norsys.documentai.documents.dtos.DocumentResponse;
import fr.norsys.documentai.documents.entities.Document;
import fr.norsys.documentai.documents.repositories.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

    public Page<DocumentResponse> getDocumentsPaginated(Pageable pageable) {
        Page<Document> documentsPage = documentRepository.findAll(pageable);

        return documentsPage.map(DocumentResponse::new);
    }
}
