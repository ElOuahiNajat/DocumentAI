package fr.norsys.documentai.documents.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.norsys.documentai.documents.dtos.DocumentResponse;
import fr.norsys.documentai.documents.dtos.UpdateDocumentRequest;
import fr.norsys.documentai.documents.services.DocumentService;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonPathValueAssert;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResultAssert;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
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

    private DocumentResponse documentResponse;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        documentResponse = new DocumentResponse(
                UUID.randomUUID(),
                "Test Title",
                "Test Author",
                "Test Description",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                "PDF",
                3000,
                "2.93 Mo"
        );
        pageable = PageRequest.of(0, 10);
        
    }


    @Test
    void listDocuments_withFilterNone_shouldReturnPaginatedDocumentsResponse() {
        // Arrange
        Page<DocumentResponse> page = new PageImpl<>(List.of(documentResponse), pageable, 1);
        when(documentService.getDocuments("none", pageable)).thenReturn(page);
        // Act & Assert
        MvcTestResultAssert resultAssert = mockMvc
                .get()
                .uri(ENDPOINT + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize() + "&filter=none")
                .assertThat();

        resultAssert.hasStatusOk();
        JsonPathValueAssert responseContent = resultAssert.bodyJson().extractingPath("$.content");
        responseContent.asArray().hasSize(1);
        responseContent
                .convertTo(InstanceOfAssertFactories.list(DocumentResponse.class))
                .element(0)
                .satisfies(document -> assertThat(document).isEqualTo(documentResponse));

        // Verify        
        verify(documentService, times(1)).getDocuments("none", pageable);

    }


    @Test
    void listDocuments_withFilterGreaterThan2Mo_shouldReturnFilteredPaginatedResponse() {
        // Arrange
        Page<DocumentResponse> page = new PageImpl<>(List.of(documentResponse), pageable, 1);
        when(documentService.getDocuments("greaterThan2Mo", pageable)).thenReturn(page);

        // Act & Assert
        MvcTestResultAssert resultAssert = mockMvc
                .get()
                .uri(ENDPOINT + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize() + "&filter=greaterThan2Mo")
                .assertThat();

        resultAssert.hasStatusOk();
        JsonPathValueAssert responseContent = resultAssert.bodyJson().extractingPath("$.content");
        responseContent.asArray().hasSize(1);

        // Verify
        verify(documentService, times(1)).getDocuments("greaterThan2Mo", pageable);
    }
    
    @Test
    void listDocuments_withFilterCreatedToday_shouldReturnFilteredPaginatedResponse() {
        // Arrange
        Page<DocumentResponse> page = new PageImpl<>(List.of(documentResponse), pageable, 1);
        when(documentService.getDocuments("createdToday", pageable)).thenReturn(page);

        // Act & Assert
        MvcTestResultAssert resultAssert = mockMvc
                .get()
                .uri(ENDPOINT + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize() + "&filter=createdToday")
                .assertThat();

        resultAssert.hasStatusOk();
        JsonPathValueAssert responseContent = resultAssert.bodyJson().extractingPath("$.content");
        responseContent.asArray().hasSize(1);

        // Verify
        verify(documentService, times(1)).getDocuments("createdToday", pageable);
    }


    @Test
    void listDocuments_withFilterUpdatedBeforeToday_shouldReturnFilteredPaginatedResponse() {
        // Arrange
        Page<DocumentResponse> page = new PageImpl<>(List.of(documentResponse), pageable, 1);
        when(documentService.getDocuments("updatedBeforeToday", pageable)).thenReturn(page);

        // Act & Assert
        MvcTestResultAssert resultAssert = mockMvc
                .get()
                .uri(ENDPOINT + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize() + "&filter=updatedBeforeToday")
                .assertThat();

        resultAssert.hasStatusOk();
        JsonPathValueAssert responseContent = resultAssert.bodyJson().extractingPath("$.content");
        responseContent.asArray().hasSize(1);

        // Verify
        verify(documentService, times(1)).getDocuments("updatedBeforeToday", pageable);
    }


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
