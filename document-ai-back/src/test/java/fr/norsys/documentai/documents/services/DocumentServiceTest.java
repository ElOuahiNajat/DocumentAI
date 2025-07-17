package fr.norsys.documentai.documents.services;

import fr.norsys.documentai.documents.dtos.DocumentResponse;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
    void getDocumentsPaginated_shouldReturnPageOfDocumentsResponse() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        UUID documentId = UUID.randomUUID();

        Document document = Document.builder()
                .id(documentId)
                .title("Test Document")
                .author("John")
                .description("Test Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .fileType("PDF")
                .fileSize(1024)
                .filePath("/document.pdf")
                .build();

        List<Document> documents = List.of(document);
        Page<Document> documentPage = new PageImpl<>(documents, pageable, documents.size());

        when(documentRepository.findAll(pageable)).thenReturn(documentPage);

        // Act
        Page<DocumentResponse> result = documentService.getDocumentsPaginated(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        DocumentResponse documentResponse = result.getContent().get(0);
        assertEquals(document.getId(), documentResponse.id());
        assertEquals(document.getTitle(), documentResponse.title());
        assertEquals(document.getAuthor(), documentResponse.author());
        assertEquals(document.getDescription(), documentResponse.description());
        assertEquals(document.getFileType(), documentResponse.fileType());
        assertEquals(document.getFileSize(), documentResponse.fileSize());

        verify(documentRepository, times(1)).findAll(pageable);
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
    void shouldThrowExceptionWhenDocumentNotFound() {
        // Given
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
}
