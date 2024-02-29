package edu.java.bot.messageProcessors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dao.UserDAO;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class UserMessageProcessor {
    protected final List<? extends Command> commands;

    public Command getHelpCommand() {
        for (Command command : commands) {
            if (command instanceof HelpCommand) {
                return command;
            }
        }
        return null;
    }

    public UserMessageProcessor(@Autowired UserDAO userDAO) {
        this.commands = List.of(
            new StartCommand(userDAO),
            new HelpCommand(userDAO),
            new ListCommand(userDAO),
            new TrackCommand(userDAO),
            new UntrackCommand(userDAO)
        );
    }

    public abstract SendMessage process(Update update);
}
