package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.UtilsForTests;
import edu.java.bot.client.ScrapperClient;
import java.net.URI;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TrackCommandTest {

    @Test
    public void testReturnTextAfterSuccessfulTrackingOfLink(){
        long chatId = 1L;
        URI link = URI.create("https://github.com/getify/You-Dont-Know-JS");
        Update mockUpdate = UtilsForTests.getMockUpdate(chatId);
        Mockito.when(mockUpdate.message().text()).thenReturn("/track %s".formatted(link));
        ScrapperClient mockScrapperClient = Mockito.mock(ScrapperClient.class);
        Mockito.doNothing().when(mockScrapperClient).trackLink(chatId, link);
        TrackCommand trackCommand = new TrackCommand(mockScrapperClient);

        String actual = trackCommand.handle(mockUpdate).getParameters().get("text").toString();

        assertEquals("Теперь ваша ссылка отслеживается", actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/track https://github.com",
        "/track",
        "/track       ",
        "/track https://github.com/getify/You-Dont-Know-JS https://github.com/getify/You-Dont-Know-JS"
    })
    public void testReturnWarningTextAfterTryingOfTrackInvalidLink(String invalidCommand){
        long chatId = 1L;
        Update mockUpdate = UtilsForTests.getMockUpdate(chatId);
        Mockito.when(mockUpdate.message().text()).thenReturn(invalidCommand);
        ScrapperClient mockScrapperClient = Mockito.mock(ScrapperClient.class);
        Mockito.doNothing().when(mockScrapperClient).trackLink(chatId, URI.create("somelinlk"));
        TrackCommand trackCommand = new TrackCommand(mockScrapperClient);

        String actual = trackCommand.handle(mockUpdate).getParameters().get("text").toString();

        assertNotEquals("Теперь ваша ссылка отслеживается", actual);
    }
}
