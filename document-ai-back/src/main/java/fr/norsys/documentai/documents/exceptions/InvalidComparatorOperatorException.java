package fr.norsys.documentai.documents.exceptions;

public class InvalidComparatorOperatorException extends RuntimeException {
    public InvalidComparatorOperatorException(String operator) {
        super("Invalid fileSizeOperator value: " + operator);
    }
}
