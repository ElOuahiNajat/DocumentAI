package fr.norsys.documentai.users.exceptions;

import fr.norsys.documentai.exceptions.ResourceBadRequestException;

public class UserNotFoundException extends ResourceBadRequestException {
    public UserNotFoundException(String message) {
        super(message);
    }
}