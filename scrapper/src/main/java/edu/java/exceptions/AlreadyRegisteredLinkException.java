package edu.java.exceptions;

import java.net.URI;

public class AlreadyRegisteredLinkException extends AlreadyRegisteredDataException {
    private static final String MESSAGE = "Link %s already registered";

    public AlreadyRegisteredLinkException(URI link) {
        super(MESSAGE.formatted(link.toString()));
    }
}
