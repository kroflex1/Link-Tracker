package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import java.util.List;

public class HelpCommand extends Command {
    public HelpCommand(ScrapperClient scrapperClient) {
        super(scrapperClient);
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Вывести список доступных команд";
    }

    @Override
    public SendMessage handle(Update update) {
        StringBuilder resultText = new StringBuilder();
        for (Command command : getCommands()) {
            if (command instanceof CompositeCommand) {
                resultText.append(String.format(
                    "%s %s - %s",
                    command.command(),
                    ((CompositeCommand) command).argumentName(),
                    command.description()
                )).append("\n");
            } else {
                resultText.append(String.format("%s - %s", command.command(), command.description())).append("\n");
            }
        }
        return new SendMessage(update.message().chat().id(), resultText.toString());
    }

    private List<Command> getCommands() {
        return List.of(
            new HelpCommand(scrapperClient),
            new ListCommand(scrapperClient),
            new StartCommand(scrapperClient),
            new TrackCommand(scrapperClient),
            new UntrackCommand(scrapperClient)
        );
    }
}
