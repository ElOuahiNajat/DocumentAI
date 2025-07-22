package fr.norsys.documentai.users.exceptions;

import fr.norsys.documentai.exceptions.ResourceBadRequestException;

public class UserAlreadyExistException extends ResourceBadRequestException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
