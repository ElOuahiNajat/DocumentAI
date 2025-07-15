package fr.norsys.documentai.documents.exceptions;


public class PageNotFoundException extends RuntimeException {

    public PageNotFoundException(String message) {
        super(message);
    }
}
