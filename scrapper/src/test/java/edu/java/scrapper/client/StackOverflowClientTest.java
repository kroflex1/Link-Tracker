package edu.java.scrapper.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.StackOverflowClient;
import edu.java.client.inforamation.QuestionInformation;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import edu.java.retryPolicy.RetryPolicy;
import edu.java.utils.TimeManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WireMockTest(httpPort = 8080)
public class StackOverflowClientTest {
    private static final Retry retryPolicy = RetryPolicy.CONSTANT.createWith(1, Duration.ofSeconds(1));
    private static final StackOverflowClient STACK_OVERFLOW_CLIENT =
        new StackOverflowClient("http://localhost:8080", new HttpHeaders(), retryPolicy);

    @ParameterizedTest
    @MethodSource("getResponses")
    public void testGetInformationAboutQuestion(
        QuestionInformation questionInf, WireMockRuntimeInfo wmRuntimeInfo
    ) {
        WireMock wireMock = wmRuntimeInfo.getWireMock();
        wireMock.loadMappingsFrom("src/test/resources/stackoverflow-wiremock-templates/");

        QuestionInformation actual = STACK_OVERFLOW_CLIENT.getInformationAboutQuestion(questionInf.getId()).get();

        assertEquals(questionInf, actual);
    }

    @Test
    public void testCantGetInformationAboutQuestion() {
        String pathForQuestion = String.join("/", "/questions", "1");
        stubFor(get(urlPathMatching(pathForQuestion))
            .withQueryParam("site", equalTo("stackoverflow"))
            .willReturn(aResponse()
                .withStatus(404)));

        Optional<QuestionInformation> actual = STACK_OVERFLOW_CLIENT.getInformationAboutQuestion(1);

        assertTrue(actual.isEmpty());
    }


    static Stream<Arguments> getResponses() {
        Arguments lastAnswerData = createResponseAndAdditionalInformation(
            1000L,
            "Limus",
            "Use python with the IDE",
            "http://some-link-to-answer"
        );
        QuestionInformation.AdditionalInformation lastAnswer =
            (QuestionInformation.AdditionalInformation) lastAnswerData.get()[0];

        Arguments lastCommentData = createResponseAndAdditionalInformation(
            500L,
            "Mark",
            "Which version of Python are you using?",
            "http://some-link-to-comment"
        );
        QuestionInformation.AdditionalInformation lastComment =
            (QuestionInformation.AdditionalInformation) lastCommentData.get()[0];

        Long questionId = 1L;
        String questionText = "How to use python?";
        Long creationEpochTime = 100L;
        Long lastActivityEpochTime = 1000L;
        OffsetDateTime creationDate = TimeManager.convertEpochToOffsetDateTime(creationEpochTime);
        OffsetDateTime lastActivityDate = TimeManager.convertEpochToOffsetDateTime(lastActivityEpochTime);
        QuestionInformation questionInformation = QuestionInformation.builder()
            .id(questionId)
            .text(questionText)
            .creationDate(creationDate)
            .lastUpdateTime(lastActivityDate)
            .lastAnswer(lastAnswer)
            .lastComment(lastComment)
            .build();

        return Stream.of(
            Arguments.of(questionInformation));
    }

    private static Arguments createResponseAndAdditionalInformation(
        Long epochTime,
        String ownerName,
        String text,
        String link
    ) {
        OffsetDateTime dateTime = TimeManager.convertEpochToOffsetDateTime(epochTime);
        QuestionInformation.AdditionalInformation additionalInf = new QuestionInformation.AdditionalInformation(
            dateTime,
            ownerName,
            text,
            link
        );
        String response =
            String.format(
                "{\"items\":[{\"creation_date\":%d,\"body\":\"%s\",\"link\":\"%s\",\"owner\":{\"display_name\":\"%s\"}}]}",
                epochTime,
                text,
                link,
                ownerName
            );
        return Arguments.of(additionalInf, response);
    }

}
