package edu.java.bot.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import edu.java.response.LinkResponse;
import edu.java.response.ListLinksResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest(httpPort = 8080)
public class ScrapperClientTest {

    private static final ScrapperClient SCRAPPER_CLIENT = new ScrapperClient("http://localhost:8080");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("listLinkResponses")
    public void testGetTrackedLinks(ListLinksResponse responseBody) throws JsonProcessingException {
        stubFor(get("/links/%d".formatted(1))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(OBJECT_MAPPER.writeValueAsString(responseBody))));
        List<URI> expected = SCRAPPER_CLIENT.getTrackedLinks(1L);

        List<URI> actual = responseBody.links().stream().map(LinkResponse::link).toList();

        Assertions.assertIterableEquals(actual, expected);
    }

    @Test
    public void testCantGetTrackedLinks() {
        stubFor(get("/links/1")
            .willReturn(aResponse().withStatus(400)));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            SCRAPPER_CLIENT.getTrackedLinks(1L));
        assertEquals("Chat with this id isn`t registered", exception.getMessage());

    }

    private static Stream<Arguments> listLinkResponses() {
        List<LinkResponse> firstLinks = List.of(new LinkResponse(1L, URI.create("somelink")));
        List<LinkResponse> secondLinks = List.of(
            new LinkResponse(1L, URI.create("someLink")),
            new LinkResponse(1L, URI.create("anotherLink"))
        );
        List<LinkResponse> thirdLinks = new ArrayList<>();
        return Stream.of(
            Arguments.of(new ListLinksResponse(firstLinks.size(), firstLinks)),
            Arguments.of(new ListLinksResponse(secondLinks.size(), secondLinks)),
            Arguments.of(new ListLinksResponse(thirdLinks.size(), thirdLinks))
        );
    }
}
