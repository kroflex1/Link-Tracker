package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.messageProcessors.UserMessageProcessor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final UserMessageProcessor userMessageProcessor;

    public ChatController(@Autowired TelegramBot telegramBot, @Autowired UserMessageProcessor userMessageProcessor) {
        this.telegramBot = telegramBot;
        this.telegramBot.setUpdatesListener(this);
        this.telegramBot.execute(new SetMyCommands(userMessageProcessor.getHelpCommand().toApiCommand()));
        this.userMessageProcessor = userMessageProcessor;
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            SendMessage sendMessage = userMessageProcessor.process(update);
            execute(sendMessage);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        telegramBot.execute(request);
    }
}
