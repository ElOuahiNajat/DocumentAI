package fr.norsys.documentai.users.exceptions;

import fr.norsys.documentai.exceptions.ResourceAlreadyExistException;

public class UserAlreadyExistException extends ResourceAlreadyExistException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
