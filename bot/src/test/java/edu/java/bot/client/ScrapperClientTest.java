//package edu.java.bot.client;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.tomakehurst.wiremock.junit5.WireMockTest;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpHeaders;
//import java.time.OffsetDateTime;
//import java.util.Map;
//import java.util.Optional;
//import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
//import static com.github.tomakehurst.wiremock.client.WireMock.get;
//import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
//import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@WireMockTest(httpPort = 8080)
//public class ScrapperClientTest {
//
//    private static final ScrapperClient SCRAPPER_CLIENT = new ScrapperClient("http://localhost:8080");
//    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
//
//    @Test
//    public void testGetTrackedLinks() throws JsonProcessingException {
//        Map<String, String> body = Map.of("name", REPOSITORY_NAME, "pushed_at", UPDATE_TIME);
//        stubFor(get("/links/%d"))
//            .willReturn(aResponse()
//                .withStatus(200)
//                .withHeader("Content-Type", "application/json")
//                .withBody(OBJECT_MAPPER.writeValueAsString(firstParameters))));
//
//        stubFor(get(urlPathMatching(String.join("/", "/repos", OWNER_NAME, REPOSITORY_NAME, "activity")))
//            .willReturn(aResponse()
//                .withStatus(200)
//                .withHeader("Content-Type", "application/json")
//                .withBody("[{\"activity_type\":\"push\"}]")));
//
//        RepositoryInformation expected =
//            new RepositoryInformation(
//                REPOSITORY_NAME,
//                OffsetDateTime.parse(UPDATE_TIME),
//                RepositoryInformation.GithubActivity.PUSH
//            );
//        RepositoryInformation actual = GITHUB_CLIENT.getRepositoryInformation(OWNER_NAME, REPOSITORY_NAME).get();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    @DisplayName("Return null object after trying to get information about private or non-existent repository")
//    public void testTryGetInformationAboutUnknownRepository() {
//        stubFor(get(urlPathMatching("/repos/some-user/unknown-rep"))
//            .willReturn(aResponse().withStatus(404)));
//        stubFor(get(urlPathMatching("/repos/some-user/unknown-rep/activity"))
//            .willReturn(aResponse().withStatus(404)));
//        Optional<RepositoryInformation> actual = GITHUB_CLIENT.getRepositoryInformation(OWNER_NAME, REPOSITORY_NAME);
//        assertTrue(actual.isEmpty());
//    }
//
//    @Test
//    @DisplayName("Exception will not be triggered when unknown type of activity is received")
//    public void testProcessUnknownTypeOfRepositoryActivity() throws JsonProcessingException {
//        Map<String, String> firstParameters = Map.of("name", REPOSITORY_NAME, "pushed_at", UPDATE_TIME);
//        stubFor(get(urlPathMatching(String.join("/", "/repos", OWNER_NAME, REPOSITORY_NAME)))
//            .willReturn(aResponse()
//                .withStatus(200)
//                .withHeader("Content-Type", "application/json")
//                .withBody(OBJECT_MAPPER.writeValueAsString(firstParameters))));
//        stubFor(get(urlPathMatching(Satring.join("/", "/repos", OWNER_NAME, REPOSITORY_NAME, "activity")))
//            .willReturn(aResponse()
//                .withStatus(200)
//                .withHeader("Content-Type", "application/json")
//                .withBody("[{\"activity_type\":\"unknown_activity\"}]")));
//        RepositoryInformation actual = GITHUB_CLIENT.getRepositoryInformation(OWNER_NAME, REPOSITORY_NAME).get();
//        assertEquals(RepositoryInformation.GithubActivity.UNKNOWN, actual.getLastActivity());
//    }
//}
