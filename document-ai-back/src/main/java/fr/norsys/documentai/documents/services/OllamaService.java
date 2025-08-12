package fr.norsys.documentai.documents.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.norsys.documentai.documents.dtos.DocumentInfoDto;
import fr.norsys.documentai.documents.exceptions.DocumentAnalysisException;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OllamaService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Tika tika = new Tika();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MessageSource messageSource;
//model specification
    private static final int MAX_TOKENS = 2048;
    private static final int CHARS_PER_TOKEN = 4;
    private static final int PROMPT_OVERHEAD = 300;
    private static final int MAX_CONTENT_CHARS = (MAX_TOKENS - PROMPT_OVERHEAD) * CHARS_PER_TOKEN;

    public DocumentInfoDto describeDocument(MultipartFile file) {
        try {
            String content = tika.parseToString(file.getInputStream());

            boolean wasTruncated = false;
            if (content.length() > MAX_CONTENT_CHARS) {
                content = content.substring(0, MAX_CONTENT_CHARS);
                wasTruncated = true;
            }

            String prompt = """
                Analyze the document below and respond with ONLY valid JSON in this exact format (no markdown, no code blocks, no extra text):
                {"description": "This document talks about ...", "author": "Author name or Unknown if not found", "title": "Document title or Unknown if not found"}

                Extract the author information from document metadata, headers, signatures, or any author references in the text.
                Extract the title from document headers, metadata, or prominent text at the beginning.
                """ + (wasTruncated ? "\nNote: This document was truncated to fit processing limits. Analyze the available content.\n" : "") + """

                Document:
                """ + content;

            String model = "gemma3:1b";

            var requestBody = objectMapper.createObjectNode();
            requestBody.put("model", model);
            requestBody.put("prompt", prompt);
            requestBody.put("stream", false);
            requestBody.put("format", "json");
            requestBody.put("system", "You are a document analyzer. Respond only with valid JSON. Do not use markdown formatting or code blocks.");

            var options = objectMapper.createObjectNode();
            options.put("num_ctx", MAX_TOKENS);
            options.put("temperature", 0.1);
            requestBody.set("options", options);

            String json = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new DocumentAnalysisException(
                        messageSource.getMessage("ollama.api.call.failed", null, Locale.getDefault())
                );
            }

            return extractDocumentInfo(response.body());
        } catch (Exception e) {
            throw new DocumentAnalysisException(
                    messageSource.getMessage("document.analyse.failed", null, Locale.getDefault())
            );
        }
    }

    private DocumentInfoDto extractDocumentInfo(String body) {
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode responseNode = root.get("response");

            if (responseNode == null || responseNode.isNull()) {
                throw new DocumentAnalysisException("No 'response' field found in API response");
            }

            String responseText = responseNode.asText().trim();
            responseText = cleanJsonResponse(responseText);

            if (responseText.startsWith("{")) {
                try {
                    JsonNode innerJson = objectMapper.readTree(responseText);
                    String description = getJsonFieldValue(innerJson, "description", "Could not extract description");
                    String author = getJsonFieldValue(innerJson, "author", "Unknown");
                    String title = getJsonFieldValue(innerJson, "title", "Unknown");
                    return new DocumentInfoDto(description, author, title);
                } catch (Exception innerEx) {
                    throw new DocumentAnalysisException("Failed to parse JSON from response: " + responseText);
                }
            }

            return new DocumentInfoDto(responseText, "Unknown", "Unknown");

        } catch (Exception e) {
            throw new DocumentAnalysisException("Error parsing API response: " + e.getMessage());
        }
    }

    private String getJsonFieldValue(JsonNode jsonNode, String fieldName, String defaultValue) {
        JsonNode fieldNode = jsonNode.get(fieldName);
        if (fieldNode != null && !fieldNode.isNull()) {
            return fieldNode.asText().trim();
        }

        return defaultValue;
    }

    private String cleanJsonResponse(String responseText) {
        // Clean common markdown/code block wrappers
        responseText = responseText.replaceAll("^```(?:json)?\\s*", "");
        responseText = responseText.replaceAll("```\\s*$", "");
        responseText = responseText.replaceAll("^json\\s*", "");
        responseText = responseText.replaceAll("^`+|`+$", "");

        return responseText.trim();
    }
}
