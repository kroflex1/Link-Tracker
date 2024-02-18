package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.DAO.UserDAO;
import edu.java.bot.handler.GitHubHandler;
import edu.java.bot.handler.StackOverflowHandler;
import edu.java.bot.handler.UrlHandler;

public class TrackCommand extends CompositeCommand {
    private static final String OK_MESSAGE = "Теперь ваша ссылка отслеживается";
    private final UrlHandler urlHandler;

    public TrackCommand(UserDAO userDAO) {
        super(userDAO);
        urlHandler = new GitHubHandler();
        urlHandler.setNextUrlHandler(new StackOverflowHandler());
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
            return new SendMessage(update.message().chat().id(), getWrongLinkFormatMessage());
        }
        String link = partsOfCommand[1];
        if (!urlHandler.isValidLink(link)) {
            return new SendMessage(update.message().chat().id(), getWrongLinkFormatMessage());
        }
        Long userId = update.message().from().id();
        userDAO.addTrackedLink(userId, link);
        return new SendMessage(update.message().chat().id(), OK_MESSAGE);
    }

    private String getWrongLinkFormatMessage() {
        StringBuilder text = new StringBuilder("Неверный формат ссылки, бот поддеживает следующие форматы ссылок:\n");
        int index = 1;
        UrlHandler currentUrlHandler = urlHandler;
        while (currentUrlHandler != null) {
            text.append(index).append(") ").append(currentUrlHandler.formatDescription()).append("\n");
            currentUrlHandler = currentUrlHandler.getNextUrlHandler();
            index++;
        }
        return text.toString();
    }
}
