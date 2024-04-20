package edu.java.exceptions;

import java.net.URI;

public class AlreadyTrackedLinkException extends AlreadyRegisteredDataException {
    private static final String MESSAGE = "Chat with id=%d already tracking link %s";

    public AlreadyTrackedLinkException(Long chatId, URI link) {
        super(MESSAGE.formatted(chatId, link));
    }
}
