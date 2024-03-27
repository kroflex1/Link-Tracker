package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.UtilsForTests;
import edu.java.bot.client.ScrapperClient;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListCommandTest {
    @Test
    public void testReturnListOfTrackedLinks() {
        long chatId = 1L;
        List<URI> expectedTrackedLinks =
            List.of(URI.create("firstLink"), URI.create("secondLink"), URI.create("thirdLink"));
        StringBuilder expectedMessage = new StringBuilder("Список отслеживаемых ссылок:\n");
        for (int i = 0; i < expectedTrackedLinks.size(); i++) {
            expectedMessage.append(String.format("%d) %s\n", i + 1, expectedTrackedLinks.get(i)));
        }
        ScrapperClient mockScrapperClient = Mockito.mock(ScrapperClient.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(chatId);
        Mockito.when(mockScrapperClient.getTrackedLinks(chatId)).thenReturn(expectedTrackedLinks);
        ListCommand listCommand = new ListCommand(mockScrapperClient);

        String actualMessage = listCommand.handle(mockUpdate).getParameters().get("text").toString();

        assertEquals(expectedMessage.toString(), actualMessage);
    }

    @Test
    public void testReturnSpecialMessageWhenListOfLinksIsEmpty() {
        long chatId = 1L;
        ScrapperClient mockScrapperClient = Mockito.mock(ScrapperClient.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(chatId);
        Mockito.when(mockScrapperClient.getTrackedLinks(chatId)).thenReturn(new ArrayList<>());
        ListCommand listCommand = new ListCommand(mockScrapperClient);

        String actualMessage = listCommand.handle(mockUpdate).getParameters().get("text").toString();

        assertEquals("У вас нет отслеживаемых ссылок", actualMessage);
    }

}
