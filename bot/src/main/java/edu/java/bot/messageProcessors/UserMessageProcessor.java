package edu.java.bot.messageProcessors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import java.util.List;

public class UserMessageProcessor {
    protected final List<? extends Command> commands;

    public UserMessageProcessor(List<? extends Command> commands) {
        this.commands = commands;
    }

    public SendMessage process(Update update) {
        for (Command command : commands) {
            if (!update.message().text().isEmpty() && command.supports(update)) {
                return command.handle(update);
            }
        }
        return new SendMessage(
            update.message().chat().id(),
            "Неизвестная комада. Введите /help, чтобы получить список доступных команд"
        );
    }

    public Command getHelpCommand() {
        for (Command command : commands) {
            if (command instanceof HelpCommand) {
                return command;
            }
        }
        return null;
    }
}
