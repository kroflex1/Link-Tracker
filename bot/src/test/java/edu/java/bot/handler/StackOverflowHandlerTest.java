package edu.java.bot.handler;

import edu.java.bot.linkChecker.StackOverflowLinkChecker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StackOverflowHandlerTest {
    StackOverflowLinkChecker stackOverflowHandler = new StackOverflowLinkChecker();

    @ParameterizedTest
    @ValueSource(strings = {"", "hello", "stackoverflow.com", "https://stackoverflow.com",
        "https://stackoverflow.com/questions",
        "https://stackoverflow.com /    questions      / 123123/    what-is-the-operator-in-c",
        "https://stackoverflow.com /questions/abcd/what-is-the-operator-in-c",
    })
    void testInvalidURL(String link) {
        assertFalse(stackOverflowHandler.isValidLink(link));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
        "https://stackoverflow.com/questions/1642028",
    })
    void testValidURL(String link) {
        assertTrue(stackOverflowHandler.isValidLink(link));
    }
}
