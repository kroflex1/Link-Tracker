package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.GitHubClient;
import edu.java.client.inforamtion.RepositoryInformation;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WireMockTest(httpPort = 8080)
public class GitHubClientTest {
    private static final GitHubClient GITHUB_CLIENT = new GitHubClient("http://localhost:8080", new HttpHeaders());
    private static final String OWNER_NAME = "test-user";
    private static final String REPOSITORY_NAME = "test-repos";
    private static final String UPDATE_TIME = "2024-02-18T18:26:28Z";

    @Test
    public void testGetInformationAboutRepository(WireMockRuntimeInfo wmRuntimeInfo) {
        WireMock wireMock = wmRuntimeInfo.getWireMock();
        wireMock.loadMappingsFrom("src/test/resources/github-wiremock-templates/");

        RepositoryInformation expected =
            new RepositoryInformation(
                REPOSITORY_NAME,
                OffsetDateTime.parse(UPDATE_TIME),
                RepositoryInformation.GithubActivity.PUSH
            );
        RepositoryInformation actual = GITHUB_CLIENT.getRepositoryInformation(OWNER_NAME, REPOSITORY_NAME).get();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Return null object after trying to get information about private or non-existent repository")
    public void testTryGetInformationAboutUnknownRepository() {
        stubFor(get(urlPathMatching("/repos/some-user/unknown-rep"))
            .willReturn(aResponse().withStatus(404)));
        stubFor(get(urlPathMatching("/repos/some-user/unknown-rep/activity"))
            .willReturn(aResponse().withStatus(404)));

        Optional<RepositoryInformation> actual = GITHUB_CLIENT.getRepositoryInformation(OWNER_NAME, REPOSITORY_NAME);

        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Exception will not be triggered when unknown type of activity is received")
    public void testProcessUnknownTypeOfRepositoryActivity(WireMockRuntimeInfo wmRuntimeInfo) {
        WireMock wireMock = wmRuntimeInfo.getWireMock();
        wireMock.loadMappingsFrom("src/test/resources/github-wiremock-templates/");

        stubFor(get(urlPathMatching(String.join("/", "/repos", OWNER_NAME, REPOSITORY_NAME, "activity")))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[{\"activity_type\":\"unknown_activity\"}]")));

        RepositoryInformation actual = GITHUB_CLIENT.getRepositoryInformation(OWNER_NAME, REPOSITORY_NAME).get();

        assertEquals(RepositoryInformation.GithubActivity.UNKNOWN, actual.getLastActivity());
    }
}
