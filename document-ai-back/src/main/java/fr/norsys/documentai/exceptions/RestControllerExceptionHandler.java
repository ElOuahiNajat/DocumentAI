package fr.norsys.documentai.exceptions;

import fr.norsys.documentai.documents.exceptions.DocumentAnalysisException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class RestControllerExceptionHandler {
    @ExceptionHandler(ResourceBadRequestException.class)
    public ResponseEntity<Map<String, String>> resourceAlreadyExists(ResourceBadRequestException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> ResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DocumentAnalysisException.class)
    public ResponseEntity<Map<String, String>> documentAnalysisException(DocumentAnalysisException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
    }
}
