package fr.norsys.documentai.documents.controllers;

import fr.norsys.documentai.documents.dtos.DocumentResponse;
import fr.norsys.documentai.documents.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping
    public Page<DocumentResponse> listDocuments(
            @RequestParam int page,
            @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return documentService.getDocumentsPaginated(pageable);
    }
}
