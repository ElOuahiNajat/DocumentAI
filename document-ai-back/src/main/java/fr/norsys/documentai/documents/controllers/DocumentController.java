package fr.norsys.documentai.documents.controllers;

import fr.norsys.documentai.documents.dtos.DocumentResponse;
import fr.norsys.documentai.documents.dtos.UpdateDocumentRequest;
import fr.norsys.documentai.documents.services.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

   @GetMapping
    public Page<DocumentResponse> getDocuments(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String fileSizeOperator,
            @RequestParam(required = false) Long fileSize,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updatedAtEnd
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return documentService.getDocuments(
                pageable,
                fileSizeOperator,
                fileSize,
                createdAtStart,
                createdAtEnd,
                updatedAtStart,
                updatedAtEnd
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDocument(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDocumentRequest request)
    {
        documentService.updateDocument(id, request);

        return ResponseEntity.noContent().build(); 
       
    }

}
