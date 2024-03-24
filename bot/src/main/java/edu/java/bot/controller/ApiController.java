package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.request.LinkUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    private final TelegramBot telegramBot;

    public ApiController(@Autowired TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostMapping("/updates")
    @Operation(summary = "Send an update")
    public ResponseEntity updates(LinkUpdateRequest linkUpdateRequest) throws MalformedURLException,
        URISyntaxException {
        for (Long chatId : linkUpdateRequest.tgChatsId()) {
            URI url = new URL(linkUpdateRequest.url()).toURI();
            SendMessage message = new SendMessage(
                chatId,
                generateMessage(url, linkUpdateRequest.description())
            );
            sendMessageToChat(message);
        }
        return ResponseEntity.ok().build();
    }

    private String generateMessage(URI url, String description) {
        return String.format("По ссылке %s произошло обновление:\n%s", url, description);
    }

    private void sendMessageToChat(SendMessage message) {
        telegramBot.execute(message);
    }
}
