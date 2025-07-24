package fr.norsys.documentai.documents.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.norsys.documentai.documents.dtos.CreateDocumentRequest;
import fr.norsys.documentai.documents.dtos.UpdateDocumentRequest;
import fr.norsys.documentai.documents.services.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.assertj.MvcTestResultAssert;

import java.io.IOException;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
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

    @Test
    @Disabled
    void uploadDocument_shouldReturnCreatedStatus() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "Test Content".getBytes()
        );
        CreateDocumentRequest request = new CreateDocumentRequest(
                "title",
                "author",
                "desc",
                file
        );

        doNothing().when(documentService).saveDocument(request);

        // Act
        MvcTestResultAssert resultAssert = mockMvc.post()
                .uri(ENDPOINT)
                .multipart()
                .part(new MockPart("title", request.title().getBytes()))
                .part(new MockPart("author", request.author().getBytes()))
                .part(new MockPart("description", request.description().getBytes()))
                // TODO: error here needs fix !!!
                .part(new MockPart("file", file.getName(), file.getBytes()))
                .assertThat();

        // Assert
        resultAssert.hasStatus(HttpStatus.CREATED);
        verify(documentService, times(1)).saveDocument(request);
    }

     @Test
      void deleteDocument_shouldReturnNoContent_whenValidRequest() {
        // Arrange
        UUID documentId = UUID.randomUUID();
        doNothing().when(documentService).deleteDocument(documentId);

        // Act
        MvcTestResultAssert resultAssert = mockMvc.delete()
                .uri(ENDPOINT + "/" + documentId)
                .assertThat();

        // Assert
        resultAssert.hasStatus(HttpStatus.NO_CONTENT);
        verify(documentService, times(1)).deleteDocument(documentId);
        }
}
