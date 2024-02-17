package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.messageProcessors.UserMessageProcessor;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BotController implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final UserMessageProcessor userMessageProcessor;
    private final Logger log;

    public BotController(@Autowired TelegramBot telegramBot, @Autowired UserMessageProcessor userMessageProcessor) {
        this.telegramBot = telegramBot;
        this.telegramBot.setUpdatesListener(this);
        this.userMessageProcessor = userMessageProcessor;
        log = Logger.getLogger(BotController.class.getName());
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            log.info(update.message().text());
            SendMessage sendMessage = userMessageProcessor.process(update);
            execute(sendMessage);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        telegramBot.execute(request);
    }
}
