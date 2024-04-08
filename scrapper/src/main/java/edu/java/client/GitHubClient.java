package edu.java.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import edu.java.client.inforamation.RepositoryInformation;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.java.client.retry.RetryPolicy;
import org.hibernate.service.spi.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.validation.annotation.Validated;
import reactor.util.retry.Retry;

@Validated
public class GitHubClient extends HttpClient {
    private static final String DEFAULT_URL = "https://api.github.com";
    private static final String START_PATH = "/repos";
    private static final Pattern PATTERN_FOR_LINK = Pattern.compile("https://github\\.com/(.+)/(.+)");
    private static final String NOT_FOUND_REPOSITORY_MESSAGE = "Repository was not found, or it is private";
    private static final Retry DEFAULT_RETRY_POLICY = RetryPolicy.CONSTANT.createWith(2, Duration.ofSeconds(2));
    private static final Set<HttpStatusCode> codesForRetry =
        Set.of(HttpStatusCode.valueOf(500), HttpStatusCode.valueOf(502));
    private static final Map<String, RepositoryInformation.GithubActivity> GITHUB_ACTIVITY_MAPPER = Map.of(
        "branch_creation", RepositoryInformation.GithubActivity.BRANCH_CREATION,
        "push", RepositoryInformation.GithubActivity.PUSH
    );

    public GitHubClient(HttpHeaders headers) {
        super(DEFAULT_URL, headers, DEFAULT_RETRY_POLICY);
    }

    public GitHubClient(String baseUrl, HttpHeaders headers) {
        super(baseUrl, headers, DEFAULT_RETRY_POLICY);
    }

    public GitHubClient(String baseUrl, HttpHeaders headers, Retry retryPolicy) {
        super(baseUrl, headers, retryPolicy);
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
            String response = getResponse(
                String.join("/", START_PATH, owner, repositoryName),
                new LinkedMultiValueMap<>(),
                NOT_FOUND_REPOSITORY_MESSAGE,
                codesForRetry
            );
            repositoryInformation = objectMapper.readValue(response, RepositoryInformation.class);
            RepositoryInformation.GithubActivity lastGitHubActivity = getLastGithubActivity(owner, repositoryName);
            repositoryInformation.setLastActivity(lastGitHubActivity);
        } catch (IllegalArgumentException | ServiceException | JsonProcessingException e) {
            return Optional.empty();
        }
        return Optional.of(repositoryInformation);
    }

    private RepositoryInformation.GithubActivity getLastGithubActivity(
        @NotNull String owner, @NotNull String repositoryName
    )
        throws JsonProcessingException {
        String response = getResponse(
            String.join("/", START_PATH, owner, repositoryName, "activity"),
            new LinkedMultiValueMap<>(),
            NOT_FOUND_REPOSITORY_MESSAGE,
            codesForRetry
        );
        JsonNode node = objectMapper.readTree(response);
        return GITHUB_ACTIVITY_MAPPER.getOrDefault(
            node.get(0).get("activity_type").asText(),
            RepositoryInformation.GithubActivity.UNKNOWN
        );
    }
}
