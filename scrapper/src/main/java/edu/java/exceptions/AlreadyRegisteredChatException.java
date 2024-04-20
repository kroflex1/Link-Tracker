package edu.java.exceptions;

public class AlreadyRegisteredChatException extends AlreadyRegisteredDataException {
    private static final String MESSAGE = "Chat with %d already registered";

    public AlreadyRegisteredChatException(Long chatId) {
        super(MESSAGE.formatted(chatId));
    }
}
