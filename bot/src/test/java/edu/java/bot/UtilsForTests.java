package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.mockito.Mockito;

public class UtilsForTests {
    public static Update getMockUpdate(Long chatID) {
        Update mockUpdate = Mockito.mock(Update.class);
        Mockito.when(mockUpdate.message()).thenReturn(Mockito.mock(Message.class));
        Mockito.when(mockUpdate.message().chat()).thenReturn(Mockito.mock(Chat.class));
        Mockito.when(mockUpdate.message().chat().id()).thenReturn(chatID);
        return mockUpdate;
    }

}
