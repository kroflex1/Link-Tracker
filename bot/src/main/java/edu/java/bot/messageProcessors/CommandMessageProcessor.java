package edu.java.bot.messageProcessors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class CommandMessageProcessor implements UserMessageProcessor {
    private final List<Command> commands;

    public CommandMessageProcessor(@Autowired List<Command> commands) {
        this.commands = commands;
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
