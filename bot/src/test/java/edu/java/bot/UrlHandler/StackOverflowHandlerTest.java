package edu.java.bot.UrlHandler;

import edu.java.bot.handler.StackOverflowHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StackOverflowHandlerTest {
    StackOverflowHandler stackOverflowHandler = new StackOverflowHandler();

    @ParameterizedTest
    @ValueSource(strings = {"", "hello", "stackoverflow.com", "https://stackoverflow.com",
        "https://stackoverflow.com/search",
        "https://stackoverflow.com/questions",
        "https://stackoverflow.com /    search? q= unsupported%20link",
        "https://stackoverflow.com/search?q=",
        "\"https://stackoverflow.com/search?q=unsupported%20link blabla"
    })
    void testInvalidURL(String link) {
        assertFalse(stackOverflowHandler.isValidLink(link));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
        "https://stackoverflow.com/questions/1642028",
        "https://stackoverflow.com/search?q=unsupported%20link"
    })
    void testValidURL(String link) {
        assertTrue(stackOverflowHandler.isValidLink(link));
    }
}
