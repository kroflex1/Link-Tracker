package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.DAO.InMemoryUserDAO;
import edu.java.bot.DAO.UserDAO;
import edu.java.bot.commands.ListCommand;
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
        Update mockUpdate = getMockUpdate(1L);
        ListCommand listCommand = new ListCommand(mockUserDAO);
        Mockito.when(mockUserDAO.getUserById(anyLong())).thenReturn(Optional.of(new UserModel(1L)));
        String actual = listCommand.handle(mockUpdate).getParameters().get("text").toString();
        assertEquals("У вас нет отслеживаемых ссылок", actual);
    }

    @Test
    public void testReturnListOfTrackedLinks() {
        Set<String> expectedTrackedLinks = Set.of("firstLink", "secondLink", "thirrdLink");
        UserDAO mockUserDAO = Mockito.mock(InMemoryUserDAO.class);
        UserModel mockUserModel = Mockito.mock(UserModel.class);
        Update mockUpdate = getMockUpdate(1L);
        ListCommand listCommand = new ListCommand(mockUserDAO);
        Mockito.when(mockUserDAO.getUserById(anyLong())).thenReturn(Optional.of(mockUserModel));
        Mockito.when(mockUserModel.getLinks()).thenReturn(expectedTrackedLinks);
        String message = listCommand.handle(mockUpdate).getParameters().get("text").toString();
        Set<String> actualLinks = Arrays.stream(message.split("\n")).skip(1).collect(Collectors.toSet());
        assertEquals(expectedTrackedLinks, actualLinks);
    }

    private Update getMockUpdate(Long userId, Long chatID) {
        Update mockUpdate = Mockito.mock(Update.class);
        Mockito.when(mockUpdate.message()).thenReturn(Mockito.mock(Message.class));
        Mockito.when(mockUpdate.message().from()).thenReturn(Mockito.mock(User.class));
        Mockito.when(mockUpdate.message().from().id()).thenReturn(userId);
        Mockito.when(mockUpdate.message().chat()).thenReturn(Mockito.mock(Chat.class));
        Mockito.when(mockUpdate.message().chat().id()).thenReturn(chatID);
        return mockUpdate;
    }

    private Update getMockUpdate(Long userId) {
        return getMockUpdate(userId, 1L);
    }
}
