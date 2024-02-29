package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dao.InMemoryUserDAO;
import edu.java.bot.dao.UserDAO;
import edu.java.bot.UtilsForTests;
import edu.java.bot.model.UserModel;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

public class ListCommandTest {

    @Test
    public void testReturnSpecialMessageWhenListOfLinksIsEmpty() {
        UserDAO mockUserDAO = Mockito.mock(InMemoryUserDAO.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(1L);
        Mockito.when(mockUserDAO.getUserByChatId(anyLong())).thenReturn(Optional.of(new UserModel(1L)));

        ListCommand listCommand = new ListCommand(mockUserDAO);
        String actual = listCommand.handle(mockUpdate).getParameters().get("text").toString();
        assertEquals("У вас нет отслеживаемых ссылок", actual);
    }

    @Test
    public void testReturnListOfTrackedLinks() {
        Set<String> expectedTrackedLinks = Set.of("firstLink", "secondLink", "thirrdLink");
        UserDAO mockUserDAO = Mockito.mock(InMemoryUserDAO.class);
        UserModel mockUserModel = Mockito.mock(UserModel.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(1L);
        Mockito.when(mockUserDAO.getUserByChatId(anyLong())).thenReturn(Optional.of(mockUserModel));
        Mockito.when(mockUserModel.getLinks()).thenReturn(expectedTrackedLinks);

        ListCommand listCommand = new ListCommand(mockUserDAO);
        String message = listCommand.handle(mockUpdate).getParameters().get("text").toString();
        Set<String> actualLinks = Arrays.stream(message.split("\n")).skip(1).collect(Collectors.toSet());
        assertEquals(expectedTrackedLinks, actualLinks);
    }


}
