package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.DAO.InMemoryUserDAO;
import edu.java.bot.DAO.UserDAO;
import edu.java.bot.UtilsForTests;
import edu.java.bot.model.UserModel;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyLong;

public class UntrackCommandTest {
    @Test
    public void testReturnTextAfterSuccessfulUntrackLink() {
        UserDAO mockUserDAO = Mockito.mock(InMemoryUserDAO.class);
        UserModel mockUserModel = Mockito.mock(UserModel.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(1L);
        Mockito.when(mockUserDAO.getUserById(anyLong())).thenReturn(Optional.of(mockUserModel));
        Mockito.when(mockUserModel.getLinks()).thenReturn(Set.of("https://github.com/getify/You-Dont-Know-JS"));
        Mockito.when(mockUpdate.message().text()).thenReturn("/untrack https://github.com/getify/You-Dont-Know-JS");

        UntrackCommand untrackCommand = new UntrackCommand(mockUserDAO);
        String actual = untrackCommand.handle(mockUpdate).getParameters().get("text").toString();
        assertEquals("Ссылка больше не отслеживается", actual);
    }

    @Test
    public void testCantFindLinkToUntrackIt() {
        UserDAO mockUserDAO = Mockito.mock(InMemoryUserDAO.class);
        UserModel mockUserModel = Mockito.mock(UserModel.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(1L);
        Mockito.when(mockUserDAO.getUserById(anyLong())).thenReturn(Optional.of(mockUserModel));
        Mockito.when(mockUserModel.getLinks()).thenReturn(new HashSet<>());
        Mockito.when(mockUpdate.message().text()).thenReturn("/untrack https://github.com/getify/You-Dont-Know-JS");

        UntrackCommand untrackCommand = new UntrackCommand(mockUserDAO);
        String actual = untrackCommand.handle(mockUpdate).getParameters().get("text").toString();
        assertEquals("Переданная ссылка не найдена", actual);
    }
}
