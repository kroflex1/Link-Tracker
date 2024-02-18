package edu.java.bot.messageProcessors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.DAO.UserDAO;
import edu.java.bot.commands.Command;

public class CommandMessageProcessor extends UserMessageProcessor {

    public CommandMessageProcessor(UserDAO userDAO) {
        super(userDAO);
    }

    @Override
    public SendMessage process(Update update) {
        for (Command command : commands) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }
        return new SendMessage(
            update.message().chat().id(),
            "Неизвестная комада. Введите /help, чтобы получить список доступных команд"
        );
    }
}
