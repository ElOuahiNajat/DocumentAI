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
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
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

    
}
