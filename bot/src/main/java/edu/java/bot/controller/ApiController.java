package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dao.ChatDAO;
import edu.java.bot.model.ChatModel;
import edu.java.request.LinkUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    private final TelegramBot telegramBot;
    private final ChatDAO chatDAO;

    public ApiController(@Autowired TelegramBot telegramBot, @Autowired ChatDAO chatDAO) {
        this.telegramBot = telegramBot;
        this.chatDAO = chatDAO;
    }

    @PostMapping("/updates")
    @Operation(summary = "Send an update")
    public ResponseEntity updates(LinkUpdateRequest linkUpdateRequest) throws MalformedURLException,
        URISyntaxException {
        for (Long chatId : linkUpdateRequest.tgChatsId()) {
            Optional<ChatModel> userModel = chatDAO.getChatById(chatId);
            if (userModel.isEmpty()) {
                throw new IllegalArgumentException("Not found chat with id=%d ".formatted(chatId));
            }
            URI uri = new URL(linkUpdateRequest.url()).toURI();
            SendMessage message = new SendMessage(
                chatId,
                generateMessageText(uri, linkUpdateRequest.description())
            );
            sendMessageToChat(message);
        }
        return ResponseEntity.ok().build();
    }

    private String generateMessageText(URI uri, String description) {
        return String.format("По %s произошло изменение\n%s", uri.toString(), description);
    }

    private void sendMessageToChat(SendMessage message) {
        telegramBot.execute(message);
    }
}
