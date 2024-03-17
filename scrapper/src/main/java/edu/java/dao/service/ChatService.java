package edu.java.dao.service;

public interface ChatService {
    void register(long tgChatId) throws IllegalArgumentException;

    void unregister(long tgChatId);
}
