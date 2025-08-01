package fr.norsys.documentai.documents.services;

import fr.norsys.documentai.documents.dtos.CreateDocumentRequest;
import fr.norsys.documentai.documents.dtos.DocumentResponse;
import fr.norsys.documentai.documents.dtos.UpdateDocumentRequest;
import fr.norsys.documentai.documents.dtos.DownloadedDocumentDTO;
import fr.norsys.documentai.documents.dtos.FeedbackRequest;
import fr.norsys.documentai.documents.dtos.FeedbackResponse;
import fr.norsys.documentai.documents.exceptions.ExportCsvException;
import fr.norsys.documentai.documents.services.FileStorageService;
import fr.norsys.documentai.documents.entities.Document;
import fr.norsys.documentai.documents.entities.Comment;
import fr.norsys.documentai.documents.entities.Evaluation;
import fr.norsys.documentai.documents.entitySpecs.*;
import fr.norsys.documentai.documents.enums.ComparatorOperator;
import fr.norsys.documentai.documents.exceptions.DocumentNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import fr.norsys.documentai.documents.repositories.DocumentRepository;
import fr.norsys.documentai.documents.repositories.CommentRepository;
import fr.norsys.documentai.documents.repositories.EvaluationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import fr.norsys.documentai.documents.exceptions.DocumentDownloadException;
import fr.norsys.documentai.documents.exceptions.DocumentUnreadableException;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;



@Service
@RequiredArgsConstructor
@Validated
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final MessageSource messageSource;
    private final FileStorageService fileStorageService;
    private final CommentRepository commentRepository;
    private final EvaluationRepository evaluationRepository;


    public Page<DocumentResponse> getDocuments(
            Pageable pageable,
            String searchTerm,
            ComparatorOperator fileSizeComparator,
            Integer fileSize,
            LocalDate createdAtStart,
            LocalDate createdAtEnd,
            LocalDate updatedAtStart,
            LocalDate updatedAtEnd
    ) {
        List<Specification<Document>> documentSpecs = new ArrayList<>();

        if (searchTerm != null && !searchTerm.isBlank()) {
            documentSpecs.add(Specification.anyOf(
                    DocumentTitleSpecification.containsTitle(searchTerm),
                    DocumentAuthorSpecification.containsAuthor(searchTerm),
                    DocumentDescriptionSpecification.containsDescription(searchTerm),
                    DocumentFileTypeSpecification.hasFileType(searchTerm)

            ));
        }

        if (fileSize != null && fileSizeComparator != null) {
            Specification<Document> fileSizeSpec = switch (fileSizeComparator) {
                case ComparatorOperator.GREATER_THAN -> DocumentFileSizeSpecs.fileSizeGreaterThan(fileSize);
                case ComparatorOperator.GREATER_THAN_OR_EQUAL ->
                        DocumentFileSizeSpecs.fileSizeGreaterThanOrEqual(fileSize);
                case ComparatorOperator.LESS_THAN -> DocumentFileSizeSpecs.fileSizeLessThan(fileSize);
                case ComparatorOperator.LESS_THAN_OR_EQUAL -> DocumentFileSizeSpecs.fileSizeLessThanOrEqual(fileSize);
                case ComparatorOperator.EQUAL -> DocumentFileSizeSpecs.fileSizeEqual(fileSize);
            };
            documentSpecs.add(fileSizeSpec);
        }

        if (createdAtStart != null && createdAtEnd != null) {
            documentSpecs.add(DocumentDateSpecs.createdAtBetween(createdAtStart, createdAtEnd));
        }

        if (updatedAtStart != null && updatedAtEnd != null) {
            documentSpecs.add(DocumentDateSpecs.updatedAtBetween(updatedAtStart, updatedAtEnd));
        }

        Specification<Document> finalSpec = Specification.allOf(documentSpecs);

        return documentRepository.findAll(finalSpec, pageable)
                .map(doc -> new DocumentResponse(doc, new ArrayList<>()));


    }

    public void updateDocument(UUID id, UpdateDocumentRequest request) throws DocumentNotFoundException {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(
                        messageSource.getMessage("document.not.found.error", null, Locale.getDefault())
                ));
        doc.setTitle(request.title());
        doc.setAuthor(request.author());
        doc.setDescription(request.description());
        documentRepository.save(doc);


    }
    public void saveDocument(@Valid CreateDocumentRequest createDocumentRequest) throws IOException {
        MultipartFile file = createDocumentRequest.file();
        String fileType = file.getContentType();
        int fileSize = fileStorageService.getFileSizeByKo(file);

        String filePath = fileStorageService.store(file);

        Document document = Document.builder()
                .title(createDocumentRequest.title())
                .author(createDocumentRequest.author())
                .description(createDocumentRequest.description())
                .fileType(fileType)
                .fileSize(fileSize)
                .filePath(filePath)
                .build();

        documentRepository.save(document);
    }

    public void deleteDocument(UUID id) {
        documentRepository.deleteById(id);
    }

    public DownloadedDocumentDTO downloadDocument(UUID id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(
                        messageSource.getMessage("document.not.found.error", null, Locale.getDefault())
                ));
        String filePath = document.getFilePath();
        String fileName = Paths.get(filePath).getFileName().toString();
        Resource resource = fileStorageService.loadAsResource(fileName);
        return new DownloadedDocumentDTO(document, resource);
    }

    @Transactional(readOnly = true)
    public ByteArrayInputStream exportDocumentsToCSV() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStreamWriter osWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             PrintWriter writer = new PrintWriter(osWriter)) {

            writer.println("ID;Title;Author;Description;CreatedAt;FileType;FileSize");

            int chunkSize = 500;
            int page = 0;
            Page<Document> documentPage;

            do {
                documentPage = documentRepository.findAll(
                        PageRequest.of(
                                page,
                                chunkSize,
                                Sort.by(Sort.Direction.DESC, "createdAt"))
                );

                for (Document doc : documentPage.getContent()) {
                    writer.printf("%s;%s;%s;%s;%s;%s;%d%n",
                            escapeCsv(doc.getId()),
                            escapeCsv(doc.getTitle()),
                            escapeCsv(doc.getAuthor()),
                            escapeCsv(doc.getDescription()),
                            doc.getCreatedAt(),
                            escapeCsv(doc.getFileType()),
                            doc.getFileSize()
                    );
                }

                page++;

            } while (documentPage.hasNext());

            writer.flush();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("export.csv.failed", null, Locale.getDefault());
            throw new ExportCsvException(errorMessage);
        }
    }

    private String escapeCsv(Object value) {
        if (value == null) return "";
        String str = value.toString();
        if (str.contains(";") || str.contains("\"") || str.contains("\n")) {
            str = str.replace("\"", "\"\"");
            return "\"" + str + "\"";
        }
        return str;
    }

    public Resource getDocumentAsResource(UUID id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("document.not.found.error"));

        Path filePath = Paths.get(document.getFilePath());

        try {
            UrlResource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new DocumentUnreadableException("document.unreadable");
            }
        } catch (MalformedURLException e) {
            throw new DocumentDownloadException("document.download.failed");
        }
    }

    public void addFeedback(UUID documentId, FeedbackRequest feedbackRequest) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(
                        messageSource.getMessage("document.not.found.error", null, Locale.getDefault())
                ));

        if (feedbackRequest.content() != null && !feedbackRequest.content().isBlank()) {
            Comment comment = Comment.builder()
                    .content(feedbackRequest.content())
                    .document(document)
                    .build();
            commentRepository.save(comment);
        }

        if (feedbackRequest.note() != null) {
            Evaluation evaluation = Evaluation.builder()
                    .note(feedbackRequest.note())
                    .document(document)
                    .build();
            evaluationRepository.save(evaluation);
        }
    }

    public DocumentResponse getDocumentById(UUID id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(
                        messageSource.getMessage("document.not.found.error", null, Locale.getDefault())
                ));

        List<FeedbackResponse> feedbacks = new ArrayList<>();

        commentRepository.findByDocumentId(id).forEach(comment ->
            feedbacks.add(new FeedbackResponse(comment.getContent(), null, comment.getCreatedAt()))
        );

        evaluationRepository.findByDocumentId(id).forEach(evaluation ->
            feedbacks.add(new FeedbackResponse(null, evaluation.getNote(), evaluation.getCreatedAt()))
        );

        return new DocumentResponse(document, feedbacks);
    }
}


