package fr.norsys.documentai.documents.services;

import fr.norsys.documentai.documents.dtos.DocumentResponse;
import fr.norsys.documentai.documents.entities.Document;
import fr.norsys.documentai.documents.repositories.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
    @Mock
    private DocumentRepository documentRepository;
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

        DocumentResponse documentResponse = result.getContent().getFirst();
        assertEquals(document.getId(), documentResponse.id());
        assertEquals(document.getTitle(), documentResponse.title());
        assertEquals(document.getAuthor(), documentResponse.author());
        assertEquals(document.getDescription(), documentResponse.description());
        assertEquals(document.getFileType(), documentResponse.fileType());
        assertEquals(document.getFileSize(), documentResponse.fileSize());

        verify(documentRepository, times(1)).findAll(pageable);
    }
}