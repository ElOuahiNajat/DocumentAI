package fr.norsys.documentai.documents.exceptions;

import fr.norsys.documentai.exceptions.ResourceBadRequestException;

public class DocumentStorageException extends ResourceBadRequestException {
    public DocumentStorageException(String message) {
        super(message);
    }

    public DocumentStorageException() {}
}
