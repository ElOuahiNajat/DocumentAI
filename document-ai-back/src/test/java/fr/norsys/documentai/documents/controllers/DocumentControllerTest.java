package fr.norsys.documentai.documents.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.norsys.documentai.documents.dtos.UpdateDocumentRequest;
import fr.norsys.documentai.documents.services.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResultAssert;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {

    private final String ENDPOINT = "/api/documents";
  
    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private DocumentService documentService;

    @Autowired
    private ObjectMapper objectMapper;

    
    @Test
    void updateDocument_shouldReturnNoContent_whenValidRequest() throws Exception {
        // Arrange
        UUID documentId = UUID.randomUUID();

        UpdateDocumentRequest request = new UpdateDocumentRequest(
                "Updated Title",
                "Updated Author",
                "Updated Description"
        );

        doNothing().when(documentService).updateDocument(documentId, request);

        // Act
        MvcTestResultAssert resultAssert = mockMvc
                .put()
                .uri(ENDPOINT + "/" + documentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .exchange()
                .assertThat();

        // Assert
        resultAssert.hasStatus(HttpStatus.NO_CONTENT.value());
        resultAssert.body().isEmpty();

        verify(documentService, times(1)).updateDocument(eq(documentId), eq(request));
    }


}
