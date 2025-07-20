package fr.norsys.documentai.documents.exceptions;

import fr.norsys.documentai.exceptions.ResourceNotFoundException;

public class DocumentNotFoundException extends ResourceNotFoundException {
    public DocumentNotFoundException(String message) {
        super(message);
    }

    
}
