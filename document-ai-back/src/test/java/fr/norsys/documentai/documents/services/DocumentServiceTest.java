package fr.norsys.documentai.documents.services;


import fr.norsys.documentai.documents.dtos.UpdateDocumentRequest;
import fr.norsys.documentai.documents.entities.Document;
import fr.norsys.documentai.documents.exceptions.DocumentNotFoundException;
import fr.norsys.documentai.documents.repositories.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private MessageSource messageSource;  // Added this mock because your service depends on it

    @InjectMocks
    private DocumentService documentService;

    @Test
    void shouldReturnAllDocuments_whenFilterIsNone() {
        Pageable pageable = PageRequest.of(0, 10);
        Document doc = createDummyDocument();
        Page<Document> docsPage = new PageImpl<>(Collections.singletonList(doc));
        when(documentRepository.findAll(pageable)).thenReturn(docsPage);

        var result = documentService.getDocuments("none", pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(documentRepository).findAll(pageable);
    }

    @Test
    void shouldReturnDocumentsCreatedToday_whenFilterIsCreatedToday() {
        Pageable pageable = PageRequest.of(0, 10);
        Document doc = createDummyDocument();
        Page<Document> docsPage = new PageImpl<>(Collections.singletonList(doc));
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        when(documentRepository.findByCreatedAtToday(eq(start), eq(end), eq(pageable))).thenReturn(docsPage);

        var result = documentService.getDocuments("createdtoday", pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(documentRepository).findByCreatedAtToday(eq(start), eq(end), eq(pageable));
    }

    @Test
    void shouldReturnDocumentsWithMinSize_whenFilterIsGreaterThan2Mo() {
        Pageable pageable = PageRequest.of(0, 10);
        Document doc = createDummyDocument();
        Page<Document> docsPage = new PageImpl<>(Collections.singletonList(doc));
        when(documentRepository.findByFileSizeGreaterThan(DocumentService.TWO_MO_IN_KO, pageable)).thenReturn(docsPage);

        var result = documentService.getDocuments("greaterthan2mo", pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(documentRepository).findByFileSizeGreaterThan(DocumentService.TWO_MO_IN_KO, pageable);
    }

    @Test
    void shouldReturnDocumentsUpdatedBefore_whenFilterIsUpdatedBeforeToday() {
        Pageable pageable = PageRequest.of(0, 10);
        Document doc = createDummyDocument();
        Page<Document> docsPage = new PageImpl<>(Collections.singletonList(doc));
        LocalDateTime before = LocalDate.now().atStartOfDay();
        when(documentRepository.findByUpdatedAtBefore(eq(before), eq(pageable))).thenReturn(docsPage);

        var result = documentService.getDocuments("updatedbeforetoday", pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(documentRepository).findByUpdatedAtBefore(eq(before), eq(pageable));
    }

    @Test
    void shouldUpdateDocumentSuccessfully() {
        // Given
        UUID id = UUID.randomUUID();
        UpdateDocumentRequest request = new UpdateDocumentRequest("New Title", "New Author", "Updated description");

        Document existingDocument = Document.builder()
                .id(id)
                .title("Old Title")
                .author("Old Author")
                .description("Old Description")
                .filePath("/documents/file.pdf")
                .fileType("pdf")
                .fileSize(1024)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusHours(1))
                .build();

        when(documentRepository.findById(id)).thenReturn(Optional.of(existingDocument));
        when(documentRepository.save(any(Document.class))).thenReturn(existingDocument);  // can return existingDocument or updated one

        // When
        documentService.updateDocument(id, request);

        // Then
        verify(documentRepository).findById(id);
        verify(documentRepository).save(argThat(doc ->
                doc.getTitle().equals("New Title") &&
                        doc.getAuthor().equals("New Author") &&
                        doc.getDescription().equals("Updated description")
        ));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingDocument() {
        UUID id = UUID.randomUUID();
        UpdateDocumentRequest request = new UpdateDocumentRequest("Any Title", "Any Author", "Any description");

        when(documentRepository.findById(id)).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("document.not.found.error"), any(), eq(Locale.getDefault())))
                .thenReturn("Document not found");

        // Then
        DocumentNotFoundException exception = assertThrows(
                DocumentNotFoundException.class,
                () -> documentService.updateDocument(id, request)
        );

        assertEquals("Document not found", exception.getMessage());
        verify(documentRepository).findById(id);
        verify(documentRepository, never()).save(any());

    
    }

    private Document createDummyDocument() {
        Document doc = new Document();
        doc.setId(UUID.randomUUID());
        doc.setTitle("Test Document");
        doc.setAuthor("Author");
        doc.setDescription("Description");
        doc.setCreatedAt(LocalDateTime.now());
        doc.setUpdatedAt(LocalDateTime.now());
        doc.setFileType("pdf");
        doc.setFileSize(3000);
        doc.setFilePath("/documents/test.pdf");
        return doc;
    }
}
