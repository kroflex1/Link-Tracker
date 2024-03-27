package edu.java.exceptions;

public class AlreadyRegisteredDataException extends RuntimeException {
    public AlreadyRegisteredDataException(String message) {
        super(message);
    }
}
