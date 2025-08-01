package fr.norsys.documentai.documents.controllers;

import fr.norsys.documentai.documents.dtos.CreateDocumentRequest;
import fr.norsys.documentai.documents.dtos.DocumentResponse;
import fr.norsys.documentai.documents.dtos.UpdateDocumentRequest;
import fr.norsys.documentai.documents.dtos.DownloadedDocumentDTO;
import fr.norsys.documentai.documents.enums.ComparatorOperator;
import fr.norsys.documentai.documents.services.DocumentService;
import fr.norsys.documentai.documents.entities.Document;
import fr.norsys.documentai.exceptions.MethodArgumentNotValidExceptionHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;


@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController implements MethodArgumentNotValidExceptionHandler {
    private final DocumentService documentService;

    @GetMapping
    public Page<DocumentResponse> listDocuments(
            @RequestParam(required = false)String searchTerm,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) ComparatorOperator fileSizeComparator,
            @RequestParam(required = false) Integer fileSize,
            @RequestParam(required = false) LocalDate createdAtStart,
            @RequestParam(required = false) LocalDate createdAtEnd,
            @RequestParam(required = false) LocalDate updatedAtStart,
            @RequestParam(required = false) LocalDate updatedAtEnd
    ) {
        Pageable pageable = PageRequest.of(page, size);


        return documentService.getDocuments(
                pageable,
                searchTerm,
                fileSizeComparator,
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
            @Valid @RequestBody UpdateDocumentRequest request
    ) {
        documentService.updateDocument(id, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<Void> uploadDocument(
            @RequestPart String title,
            @RequestPart String author,
            @RequestPart String description,
            @RequestPart MultipartFile file
    ) throws IOException {
        CreateDocumentRequest createDocumentRequest = new CreateDocumentRequest(
            title, author, description, file
        );
        documentService.saveDocument(createDocumentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID id) {
        DownloadedDocumentDTO downloaded = documentService.downloadDocument(id);
        Document document = downloaded.document();
        Resource resource = downloaded.resource();

        String fileName = Paths.get(document.getFilePath()).getFileName().toString();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<InputStreamResource> exportDocumentsToCSV() {
        InputStreamResource resource = new InputStreamResource(documentService.exportDocumentsToCSV());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documents.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<Resource> previewDocument(@PathVariable UUID id) {
        Resource resource = documentService.getDocumentAsResource(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
