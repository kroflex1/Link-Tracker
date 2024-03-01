package edu.java.bot.messageProcessors;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dao.InMemoryChatDAO;
import edu.java.bot.UtilsForTests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandMessageProcessorTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "/heeeeelp",
        "help",
        "/truck",
        " ",
        "   "
    })
    public void testReturnSpecialMessageWhenUnknownCommandReceived(String unknownCommand) {
        CommandMessageProcessor commandMessageProcessor =
            new CommandMessageProcessor(Mockito.mock(InMemoryChatDAO.class));
        Update mockUpdate = UtilsForTests.getMockUpdate(1L);
        Mockito.when(mockUpdate.message().text()).thenReturn(unknownCommand);
        String actual = commandMessageProcessor.process(mockUpdate).getParameters().get("text").toString();
        assertEquals("Неизвестная комада. Введите /help, чтобы получить список доступных команд", actual);
    }
}
