package edu.java.bot.handler;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlHandlerTest {
    @ParameterizedTest
    @ValueSource(strings = {
        "https://github.com/getify/You-Dont-Know-JS",
        "https://github.com/getify/You-Dont-Know-JS/",
        "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
        "https://stackoverflow.com/questions/1642028",
        "https://stackoverflow.com/search?q=unsupported%20link"
    })
    public void testUrlHandlerChainCheckValidURL(String link) {
        UrlHandler urlHandler = new GitHubHandler();
        urlHandler.setNextUrlHandler(new StackOverflowHandler());
        assertTrue(urlHandler.isValidLink(link));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "github.com",
        "https://stackoverflow.com/questions",
        "https://github.com/spring-projects/spring-framework/tree/main/framework-bom"
    })
    public void testUrlHandlerChainCheckInvalidURL(String link) {
        UrlHandler urlHandler = new GitHubHandler();
        urlHandler.setNextUrlHandler(new StackOverflowHandler());
        assertFalse(urlHandler.isValidLink(link));
    }
}
