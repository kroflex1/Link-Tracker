package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.linkChecker.GitHubLinkChecker;
import edu.java.bot.linkChecker.LinkChecker;
import edu.java.bot.linkChecker.StackOverflowLinkChecker;
import org.springframework.http.ResponseEntity;
import java.net.URI;

public class TrackCommand extends CompositeCommand {
    private static final String OK_MESSAGE = "Теперь ваша ссылка отслеживается";
    private static final String ALREADY_TRACKED_LINK = "Вы уже отслеживаете данную ссылку";
    private final LinkChecker linkChecker;

    public TrackCommand(ScrapperClient scrapperClient) {
        super(scrapperClient);
        linkChecker = new GitHubLinkChecker();
        linkChecker.setNextUrlHandler(new StackOverflowLinkChecker());
    }

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String argumentName() {
        return "<url>";
    }

    @Override
    public String description() {
        return "Начать отслеживать ссылку";
    }

    @Override
    public SendMessage handle(Update update) {
        String[] partsOfCommand = update.message().text().split(" ");
        if (partsOfCommand.length != 2) {
            return new SendMessage(update.message().chat().id(), getMessageForWrongLinkFormat());
        }
        String link = partsOfCommand[1];
        if (!linkChecker.isValidLink(link)) {
            return new SendMessage(update.message().chat().id(), getMessageForWrongLinkFormat());
        }

        Long chatId = update.message().chat().id();
        try {
            scrapperClient.trackLink(chatId, URI.create(link));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals(scrapperClient.ALREADY_TRACKED_LINK_MESSAGE)) {
                return new SendMessage(chatId, ALREADY_TRACKED_LINK);
            }
            return new SendMessage(chatId, "Чтобы начать пользоваться ботом, введите команду /start");
        }
        return new SendMessage(chatId, OK_MESSAGE);
    }

    private String getMessageForWrongLinkFormat() {
        StringBuilder text = new StringBuilder("Неверный формат ссылки, бот поддеживает следующие форматы:\n");
        int index = 1;
        LinkChecker currentLinkChecker = linkChecker;
        while (currentLinkChecker != null) {
            text.append(index).append(") ").append(currentLinkChecker.linkFormatDescription()).append("\n");
            currentLinkChecker = currentLinkChecker.getNextUrlHandler();
            index++;
        }
        return text.toString();
    }
}
