package edu.java.bot.handler;

import edu.java.bot.linkChecker.GitHubLinkChecker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GitHubHandlerTest {
    GitHubLinkChecker gitHubHandler = new GitHubLinkChecker();

    @ParameterizedTest
    @ValueSource(strings = {"", "hello", "https://github.com",
        "https://github.com/sanyarnd/tinkoff-java-course-2023/   ?text=blabla",
        "https: // github.com / sanyarnd / tinkoff-java-course-2023/",
        "https://github.com/spring-projects/spring-framework/tree/main/framework-bom",
        "https://github.com/michaelklishin",
        "https://githubMcom/getify/You-Dont-Know-JS",
        "blablabla https://github.com/getify/You-Dont-Know-JS",
    })
    void testInvalidURL(String link) {
        assertFalse(gitHubHandler.isValidLink(link));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "https://github.com/getify/You-Dont-Know-JS",
        "https://github.com/getify/You-Dont-Know-JS/"
    })
    void testValidURL(String link) {
        assertTrue(gitHubHandler.isValidLink(link));
    }
}
