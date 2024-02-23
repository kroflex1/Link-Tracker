package edu.java.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.GithubActivity;
import edu.java.dto.RepositoryInformation;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Validated
public class GitHubClient {
    private static final String DEFAULT_URL = "https://api.github.com";
    private static final String NOT_FOUND_REPOSITORY_MESSAGE = "Repository was not found, or it is private";
    private static final Map<String, GithubActivity> githubActivityMapper = Map.of(
        "branch_creation", GithubActivity.BRANCH_CREATION,
        "push", GithubActivity.PUSH
    );
    private final String githubToken;
    private final WebClient webClient;
    private final String baseUrl;
    private final ObjectMapper objectMapper;

    public GitHubClient(String githubToken) {
        this(DEFAULT_URL, githubToken);
    }

    public GitHubClient(String baseUrl, String githubToken) {
        this.baseUrl = baseUrl;
        this.githubToken = githubToken;
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .defaultHeaders(httpHeaders -> {
                httpHeaders.set("Authorization", "Bearer" + this.githubToken);
                httpHeaders.set("User-Agent", "LinkTrackerBot");
                httpHeaders.set("Accept", "application/json");
            })
            .build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.findAndRegisterModules();
    }

    public RepositoryInformation getRepositoryInformation(@NotNull String owner, @NotNull String repositoryName)
        throws IllegalArgumentException {
        String response = getResponse(String.join("/", baseUrl, "repos", owner, repositoryName));
        try {
            RepositoryInformation repositoryInformation = objectMapper.readValue(response, RepositoryInformation.class);
            GithubActivity lastGitHubActivity = getLastGithubActivity(owner, repositoryName);
            repositoryInformation.setLastActivity(lastGitHubActivity);
            return repositoryInformation;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private GithubActivity getLastGithubActivity(@NotNull String owner, @NotNull String repositoryName)
        throws JsonProcessingException {
        String response = getResponse(String.join("/", baseUrl, "repos", owner, repositoryName, "activity"));
        JsonNode node = objectMapper.readTree(response);
        return githubActivityMapper.getOrDefault(node.get(0).get("activity_type").asText(), GithubActivity.UNKNOWN);
    }

    private String getResponse(String uri) {
        return webClient
            .get()
            .uri(uri)
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.NOT_FOUND,
                clientResponse -> Mono.error(new IllegalArgumentException(NOT_FOUND_REPOSITORY_MESSAGE))
            )
            .bodyToMono(String.class)
            .block();
    }
}
