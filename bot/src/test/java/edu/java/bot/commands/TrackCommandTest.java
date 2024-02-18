package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.DAO.InMemoryUserDAO;
import edu.java.bot.DAO.UserDAO;
import edu.java.bot.UtilsForTests;
import edu.java.bot.model.UserModel;
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
        UserDAO mockUserDAO = Mockito.mock(InMemoryUserDAO.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(1L);
        Mockito.when(mockUserDAO.getUserById(anyLong())).thenReturn(Optional.of(new UserModel(1L)));
        Mockito.when(mockUpdate.message().text()).thenReturn("/track https://github.com/getify/You-Dont-Know-JS");
        TrackCommand trackCommand = new TrackCommand(mockUserDAO);
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
        UserDAO mockUserDAO = Mockito.mock(InMemoryUserDAO.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(1L);
        Mockito.when(mockUserDAO.getUserById(anyLong())).thenReturn(Optional.of(new UserModel(1L)));
        Mockito.when(mockUpdate.message().text()).thenReturn(invalidCommand);
        TrackCommand trackCommand = new TrackCommand(mockUserDAO);
        String actual = trackCommand.handle(mockUpdate).getParameters().get("text").toString();
        assertNotEquals("Теперь ваша ссылка отслеживается", actual);
    }
}
