package edu.java.bot.dao;

import edu.java.bot.model.ChatModel;
import java.util.Optional;

public interface ChatDAO {
    void addChat(Long chatId);

    Optional<ChatModel> getChatById(Long chatId);

    void addTrackedLink(Long chatId, String url);

    void removeTrackedLink(Long chatId, String url);
}
