package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.UtilsForTests;
import edu.java.bot.client.ScrapperClient;
import java.net.URI;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UntrackCommandTest {
    @Test
    public void testReturnTextAfterSuccessfulUntrackLink() {
        long chatId = 1L;
        URI link = URI.create( "https://github.com/getify/You-Dont-Know-JS");
        Update mockUpdate = UtilsForTests.getMockUpdate(chatId);
        Mockito.when(mockUpdate.message().text()).thenReturn("/untrack %s".formatted(link));
        ScrapperClient mockScrapperClient = Mockito.mock(ScrapperClient.class);
        Mockito.doNothing().when(mockScrapperClient).stopTrackLink(chatId, link);
        UntrackCommand untrackCommand = new UntrackCommand(mockScrapperClient);

        String actual = untrackCommand.handle(mockUpdate).getParameters().get("text").toString();

        assertEquals("Ссылка больше не отслеживается", actual);
    }

    @Test
    public void testCantFindLinkToUntrackIt() {
        long chatId = 1L;
        URI link = URI.create( "https://github.com/getify/You-Dont-Know-JS");
        Update mockUpdate = UtilsForTests.getMockUpdate(chatId);
        Mockito.when(mockUpdate.message().text()).thenReturn("/untrack %s".formatted(link));
        ScrapperClient mockScrapperClient = Mockito.mock(ScrapperClient.class);
        Mockito.doThrow(IllegalArgumentException.class).when(mockScrapperClient).stopTrackLink(chatId, link);
        UntrackCommand untrackCommand = new UntrackCommand(mockScrapperClient);

        String actual = untrackCommand.handle(mockUpdate).getParameters().get("text").toString();

        assertEquals("Переданная ссылка не найдена", actual);
    }
}
