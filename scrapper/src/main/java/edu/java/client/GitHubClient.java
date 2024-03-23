package edu.java.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import edu.java.client.inforamtion.RepositoryInformation;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;

@Validated
public class GitHubClient extends HttpClient {
    private static final String DEFAULT_URL = "https://api.github.com";
    private static final String START_PATH = "/repos";
    private static final Pattern PATTERN_FOR_LINK = Pattern.compile("https://github\\.com/(.+)/(.+)");
    private static final String NOT_FOUND_REPOSITORY_MESSAGE = "Repository was not found, or it is private";
    private static final Map<String, RepositoryInformation.GithubActivity> GITHUB_ACTIVITY_MAPPER = Map.of(
        "branch_creation", RepositoryInformation.GithubActivity.BRANCH_CREATION,
        "push", RepositoryInformation.GithubActivity.PUSH
    );

    public GitHubClient(HttpHeaders headers) {
        this(DEFAULT_URL, headers);
    }

    public GitHubClient(String baseUrl, HttpHeaders headers) {
        super(baseUrl, headers);
    }

    public Optional<RepositoryInformation> getRepositoryInformation(@NotNull URI url) {
        Matcher matcher = PATTERN_FOR_LINK.matcher(url.toString());
        if (matcher.matches()) {
            return getRepositoryInformation(matcher.group(1), matcher.group(2));
        }
        return Optional.empty();
    }

    public Optional<RepositoryInformation> getRepositoryInformation(
        @NotNull String owner,
        @NotNull String repositoryName
    ) {
        RepositoryInformation repositoryInformation;
        try {
            String response =
                getResponse(String.join("/", START_PATH, owner, repositoryName), NOT_FOUND_REPOSITORY_MESSAGE);
            repositoryInformation = objectMapper.readValue(response, RepositoryInformation.class);
            RepositoryInformation.GithubActivity lastGitHubActivity = getLastGithubActivity(owner, repositoryName);
            repositoryInformation.setLastActivity(lastGitHubActivity);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            return Optional.empty();
        }
        return Optional.of(repositoryInformation);
    }

    private RepositoryInformation.GithubActivity getLastGithubActivity(
        @NotNull String owner,
        @NotNull String repositoryName
    )
        throws JsonProcessingException {
        String response =
            getResponse(String.join("/", START_PATH, owner, repositoryName, "activity"), NOT_FOUND_REPOSITORY_MESSAGE);
        JsonNode node = objectMapper.readTree(response);
        return GITHUB_ACTIVITY_MAPPER.getOrDefault(
            node.get(0).get("activity_type").asText(),
            RepositoryInformation.GithubActivity.UNKNOWN
        );
    }
}
