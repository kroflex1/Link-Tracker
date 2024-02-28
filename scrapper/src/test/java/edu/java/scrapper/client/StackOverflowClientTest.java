package edu.java.scrapper.client;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.StackOverflowClient;
import edu.java.dto.QuestionInformation;
import edu.java.dto.RepositoryInformation;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import edu.java.utils.TimeConvertor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WireMockTest(httpPort = 8080)
public class StackOverflowClientTest {
    private static final StackOverflowClient STACK_OVERFLOW_CLIENT =
        new StackOverflowClient("http://localhost:8080", new HttpHeaders());

    @ParameterizedTest
    @MethodSource("getResponses")
    public void testGetInformationAboutQuestion(
        QuestionInformation questionInf,
        String questionResponse,
        String lastAnswerResponse,
        String lastCommentResponse,
        int answerResponseStatusCode,
        int commentResponseStatusCode
    ) throws JsonProcessingException {

        String pathForQuestion = String.join("/", "/questions", Long.toString(questionInf.getId()));
        stubFor(get(urlPathMatching(pathForQuestion))
            .withQueryParam("site", equalTo("stackoverflow"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(questionResponse)));

        stubFor(get(urlPathMatching(String.join("/", "/questions", Long.toString(questionInf.getId()), "answers")))
            .withQueryParam("pagesize", equalTo("1"))
            .withQueryParam("order", equalTo("desc"))
            .withQueryParam("sort", equalTo("creation"))
            .withQueryParam("site", equalTo("stackoverflow"))
            .withQueryParam("filter", equalTo("!6WPIomp-eb(U5"))
            .willReturn(aResponse()
                .withStatus(answerResponseStatusCode)
                .withHeader("Content-Type", "application/json")
                .withBody(lastAnswerResponse)));

        stubFor(get(urlPathMatching(String.join("/", "/questions", Long.toString(questionInf.getId()), "comments")))
            .withQueryParam("pagesize", equalTo("1"))
            .withQueryParam("order", equalTo("desc"))
            .withQueryParam("sort", equalTo("creation"))
            .withQueryParam("site", equalTo("stackoverflow"))
            .withQueryParam("filter", equalTo("!6WPIompltQw.p"))
            .willReturn(aResponse()
                .withStatus(commentResponseStatusCode)
                .withHeader("Content-Type", "application/json")
                .withBody(lastCommentResponse)));

        QuestionInformation actual = STACK_OVERFLOW_CLIENT.getInformationAboutQuestion(questionInf.getId()).get();
        assertEquals(questionInf, actual);
    }

    @Test
    public void testCantGetInformationAboutQuestion() throws JsonProcessingException {

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
        String lastAnswerResponse = (String) lastAnswerData.get()[1];

        Arguments lastCommentData = createResponseAndAdditionalInformation(
            500L,
            "Mark",
            "Which version of Python are you using?",
            "http://some-link-to-comment"
        );
        QuestionInformation.AdditionalInformation lastComment =
            (QuestionInformation.AdditionalInformation) lastCommentData.get()[0];
        String lastCommentResponse = (String) lastCommentData.get()[1];

        Long questionId = 1L;
        String questionText = "How to use python?";
        Long creationEpochTime = 100L;
        Long lastActivityEpochTime = 1000L;
        OffsetDateTime creationDate = TimeConvertor.convertEpochToOffsetDateTime(creationEpochTime);
        OffsetDateTime lastActivityDate = TimeConvertor.convertEpochToOffsetDateTime(lastActivityEpochTime);
        QuestionInformation questionInformation = QuestionInformation.builder()
            .id(questionId)
            .text(questionText)
            .creationDate(creationDate)
            .lastActivityDate(lastActivityDate)
            .lastAnswer(lastAnswer)
            .lastComment(lastComment)
            .build();
        String questionResponse = String.format(
            "{\"items\":[{\"question_id\":%d,\"title\":\"%s\",\"creation_date\":%d,\"last_activity_date\":%d}]}",
            questionId,
            questionText,
            creationEpochTime,
            lastActivityEpochTime
        );

        return Stream.of(
            Arguments.of(questionInformation, questionResponse, lastAnswerResponse, lastCommentResponse, 202, 202));
    }

    private static Arguments createResponseAndAdditionalInformation(
        Long epochTime,
        String ownerName,
        String text,
        String link
    ) {
        OffsetDateTime dateTime = TimeConvertor.convertEpochToOffsetDateTime(epochTime);
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
