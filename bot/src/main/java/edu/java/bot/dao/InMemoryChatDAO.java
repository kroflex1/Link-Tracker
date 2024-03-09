package edu.java.bot.dao;

import edu.java.bot.model.ChatModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryChatDAO implements ChatDAO {

    private final Map<Long, ChatModel> chats = new HashMap<>();

    @Override
    public void addChat(Long chatId) {
        if (!chats.containsKey(chatId)) {
            chats.put(chatId, new ChatModel(chatId));
        }
    }

    @Override
    public Optional<ChatModel> getChatById(Long chatId) {
        if (chats.containsKey(chatId)) {
            return Optional.of(chats.get(chatId));
        }
        return Optional.empty();
    }

    @Override
    public void addTrackedLink(Long chatId, String url) throws IllegalArgumentException {
        if (!chats.containsKey(chatId)) {
            addChat(chatId);
        }
        chats.get(chatId).addLink(url);
    }

    @Override
    public void removeTrackedLink(Long chatId, String url) {
        if (chats.containsKey(chatId)) {
            addChat(chatId);
        }
        chats.get(chatId).removeLink(url);
    }
}
