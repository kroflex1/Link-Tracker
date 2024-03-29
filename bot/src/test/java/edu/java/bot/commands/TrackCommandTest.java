package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dao.InMemoryChatDAO;
import edu.java.bot.dao.ChatDAO;
import edu.java.bot.UtilsForTests;
import edu.java.bot.model.ChatModel;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyLong;

public class TrackCommandTest {

    @Test
    public void testReturnTextAfterSuccessfulTrackingOfLink(){
        ChatDAO mockChatDAO = Mockito.mock(InMemoryChatDAO.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(1L);
        Mockito.when(mockChatDAO.getChatById(anyLong())).thenReturn(Optional.of(new ChatModel(1L)));
        Mockito.when(mockUpdate.message().text()).thenReturn("/track https://github.com/getify/You-Dont-Know-JS");
        TrackCommand trackCommand = new TrackCommand(mockChatDAO);
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
        ChatDAO mockChatDAO = Mockito.mock(InMemoryChatDAO.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(1L);
        Mockito.when(mockChatDAO.getChatById(anyLong())).thenReturn(Optional.of(new ChatModel(1L)));
        Mockito.when(mockUpdate.message().text()).thenReturn(invalidCommand);
        TrackCommand trackCommand = new TrackCommand(mockChatDAO);
        String actual = trackCommand.handle(mockUpdate).getParameters().get("text").toString();
        assertNotEquals("Теперь ваша ссылка отслеживается", actual);
    }
}
