package fr.norsys.documentai.documents.controllers;

import fr.norsys.documentai.documents.dtos.DocumentResponse;
import fr.norsys.documentai.documents.services.DocumentService;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonPathValueAssert;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResultAssert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {
    private final String ENDPOINT = "/api/documents";
    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private DocumentService documentService;

    @Test
    void listDocuments_shouldReturnPaginatedDocumentsResponse() {
        // Arrange
        DocumentResponse documentResponse = new DocumentResponse(
                UUID.randomUUID(),
                "Test Title",
                "Test Author",
                "Test Description",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "PDF",
                1234
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<DocumentResponse> page = new PageImpl<>(
                List.of(documentResponse),
                pageable,
                1
        );
        when(documentService.getDocumentsPaginated(pageable)).thenReturn(page);

        // Act
        MvcTestResultAssert resultAssert = mockMvc
                .get()
                .uri(ENDPOINT + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize())
                .assertThat();

        // Assert
        resultAssert.hasStatusOk();
        JsonPathValueAssert responseContent = resultAssert.bodyJson().extractingPath("$.content");
        responseContent.asArray().hasSize(1);
        responseContent
                .convertTo(InstanceOfAssertFactories.list(DocumentResponse.class))
                .element(0)
                .satisfies(document -> assertThat(document).isEqualTo(documentResponse));
        verify(documentService, times(1)).getDocumentsPaginated(pageable);
    }
}
