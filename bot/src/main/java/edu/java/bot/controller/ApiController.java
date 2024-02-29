package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dao.UserDAO;
import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.model.UserModel;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.util.Optional;

@RestController
public class ApiController {
    private final TelegramBot telegramBot;
    private final UserDAO userDAO;

    public ApiController(@Autowired TelegramBot telegramBot, @Autowired UserDAO userDAO) {
        this.telegramBot = telegramBot;
        this.userDAO = userDAO;
    }

    @PostMapping("/updates")
    @Operation(summary = "Send an update")
    public ResponseEntity updates(LinkUpdateRequest linkUpdateRequest) {
        for (Long chatId : linkUpdateRequest.getTgChatIds()) {
            Optional<UserModel> userModel = userDAO.getUserByChatId(chatId);
            if (userModel.isEmpty()) {
                throw new IllegalArgumentException("Not found chat with id=%d ".formatted(chatId));
            }
            SendMessage message = new SendMessage(
                chatId,
                generateMessageText(linkUpdateRequest.getUrl(), linkUpdateRequest.getDescription())
            );
            sendMessageToChat(message);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    private String generateMessageText(URI url, String description) {
        return String.format("На %s произошло изменение\n%s", url.toString(), description);
    }

    private void sendMessageToChat(SendMessage message) {
        telegramBot.execute(message);
    }
}
