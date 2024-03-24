package edu.java.bot.handler;

import edu.java.bot.linkChecker.GitHubLinkChecker;
import edu.java.bot.linkChecker.LinkChecker;
import edu.java.bot.linkChecker.StackOverflowLinkChecker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LinkCheckerTest {
    @ParameterizedTest
    @ValueSource(strings = {
        "https://github.com/getify/You-Dont-Know-JS",
        "https://github.com/getify/You-Dont-Know-JS/",
        "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
        "https://stackoverflow.com/questions/1642028",
        "https://stackoverflow.com/search?q=unsupported%20link"
    })
    public void testUrlHandlerChainCheckValidURL(String link) {
        LinkChecker linkChecker = new GitHubLinkChecker();
        linkChecker.setNextUrlHandler(new StackOverflowLinkChecker());
        assertTrue(linkChecker.isValidLink(link));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "github.com",
        "https://stackoverflow.com/questions",
        "https://github.com/spring-projects/spring-framework/tree/main/framework-bom"
    })
    public void testUrlHandlerChainCheckInvalidURL(String link) {
        LinkChecker linkChecker = new GitHubLinkChecker();
        linkChecker.setNextUrlHandler(new StackOverflowLinkChecker());
        assertFalse(linkChecker.isValidLink(link));
    }
}
