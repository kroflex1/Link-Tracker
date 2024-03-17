package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dao.InMemoryChatDAO;
import edu.java.bot.dao.ChatDAO;
import edu.java.bot.UtilsForTests;
import edu.java.bot.model.ChatModel;
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
        ChatDAO mockChatDAO = Mockito.mock(InMemoryChatDAO.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(1L);
        Mockito.when(mockChatDAO.getChatById(anyLong())).thenReturn(Optional.of(new ChatModel(1L)));

        ListCommand listCommand = new ListCommand(mockChatDAO);
        String actual = listCommand.handle(mockUpdate).getParameters().get("text").toString();
        assertEquals("У вас нет отслеживаемых ссылок", actual);
    }

    @Test
    public void testReturnListOfTrackedLinks() {
        Set<String> expectedTrackedLinks = Set.of("firstLink", "secondLink", "thirrdLink");
        ChatDAO mockChatDAO = Mockito.mock(InMemoryChatDAO.class);
        ChatModel mockChatModel = Mockito.mock(ChatModel.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(1L);
        Mockito.when(mockChatDAO.getChatById(anyLong())).thenReturn(Optional.of(mockChatModel));
        Mockito.when(mockChatModel.getLinks()).thenReturn(expectedTrackedLinks);

        ListCommand listCommand = new ListCommand(mockChatDAO);
        String message = listCommand.handle(mockUpdate).getParameters().get("text").toString();
        Set<String> actualLinks = Arrays.stream(message.split("\n")).skip(1).collect(Collectors.toSet());
        assertEquals(expectedTrackedLinks, actualLinks);
    }


}
