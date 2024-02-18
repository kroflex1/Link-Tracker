package edu.java.bot.messageProcessors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.DAO.UserDAO;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public abstract class UserMessageProcessor {
    protected final List<? extends Command> commands;

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