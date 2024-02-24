package edu.java.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.RepositoryInformation;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Validated
public class GitHubClient extends HttpClient {
    private static final String DEFAULT_URL = "https://api.github.com";
    private static final String NOT_FOUND_REPOSITORY_MESSAGE = "Repository was not found, or it is private";
    private static final Map<String, RepositoryInformation.GithubActivity> githubActivityMapper = Map.of(
        "branch_creation", RepositoryInformation.GithubActivity.BRANCH_CREATION,
        "push", RepositoryInformation.GithubActivity.PUSH
    );

    public GitHubClient(HttpHeaders headers) {
        this(DEFAULT_URL, headers);
    }

    public GitHubClient(String baseUrl, HttpHeaders headers) {
        super(baseUrl, headers);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.findAndRegisterModules();
    }

    public RepositoryInformation getRepositoryInformation(@NotNull String owner, @NotNull String repositoryName)
        throws IllegalArgumentException {
        String response = getResponse(String.join("/", "/repos", owner, repositoryName), NOT_FOUND_REPOSITORY_MESSAGE);
        try {
            RepositoryInformation repositoryInformation = objectMapper.readValue(response, RepositoryInformation.class);
            RepositoryInformation.GithubActivity lastGitHubActivity = getLastGithubActivity(owner, repositoryName);
            repositoryInformation.setLastActivity(lastGitHubActivity);
            return repositoryInformation;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private RepositoryInformation.GithubActivity getLastGithubActivity(
        @NotNull String owner,
        @NotNull String repositoryName
    )
        throws JsonProcessingException {
        String response =
            getResponse(String.join("/", "/repos", owner, repositoryName, "activity"), NOT_FOUND_REPOSITORY_MESSAGE);
        JsonNode node = objectMapper.readTree(response);
        return githubActivityMapper.getOrDefault(
            node.get(0).get("activity_type").asText(),
            RepositoryInformation.GithubActivity.UNKNOWN
        );
    }
}
