package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.mockito.Mockito;

public class UtilsForTests {
    public static Update getMockUpdate(Long userId, Long chatID) {
        Update mockUpdate = Mockito.mock(Update.class);
        Mockito.when(mockUpdate.message()).thenReturn(Mockito.mock(Message.class));
        Mockito.when(mockUpdate.message().from()).thenReturn(Mockito.mock(User.class));
        Mockito.when(mockUpdate.message().from().id()).thenReturn(userId);
        Mockito.when(mockUpdate.message().chat()).thenReturn(Mockito.mock(Chat.class));
        Mockito.when(mockUpdate.message().chat().id()).thenReturn(chatID);
        return mockUpdate;
    }

    public static Update getMockUpdate(Long userId) {
        return getMockUpdate(userId, 1L);
    }
}
