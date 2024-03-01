package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dao.InMemoryChatDAO;
import edu.java.bot.dao.ChatDAO;
import edu.java.bot.UtilsForTests;
import edu.java.bot.model.ChatModel;
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
        ChatDAO mockChatDAO = Mockito.mock(InMemoryChatDAO.class);
        ChatModel mockChatModel = Mockito.mock(ChatModel.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(1L);
        Mockito.when(mockChatDAO.getChatById(anyLong())).thenReturn(Optional.of(mockChatModel));
        Mockito.when(mockChatModel.getLinks()).thenReturn(Set.of("https://github.com/getify/You-Dont-Know-JS"));
        Mockito.when(mockUpdate.message().text()).thenReturn("/untrack https://github.com/getify/You-Dont-Know-JS");

        UntrackCommand untrackCommand = new UntrackCommand(mockChatDAO);
        String actual = untrackCommand.handle(mockUpdate).getParameters().get("text").toString();
        assertEquals("Ссылка больше не отслеживается", actual);
    }

    @Test
    public void testCantFindLinkToUntrackIt() {
        ChatDAO mockChatDAO = Mockito.mock(InMemoryChatDAO.class);
        ChatModel mockChatModel = Mockito.mock(ChatModel.class);
        Update mockUpdate = UtilsForTests.getMockUpdate(1L);
        Mockito.when(mockChatDAO.getChatById(anyLong())).thenReturn(Optional.of(mockChatModel));
        Mockito.when(mockChatModel.getLinks()).thenReturn(new HashSet<>());
        Mockito.when(mockUpdate.message().text()).thenReturn("/untrack https://github.com/getify/You-Dont-Know-JS");

        UntrackCommand untrackCommand = new UntrackCommand(mockChatDAO);
        String actual = untrackCommand.handle(mockUpdate).getParameters().get("text").toString();
        assertEquals("Переданная ссылка не найдена", actual);
    }
}
