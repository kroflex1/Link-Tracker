package edu.java.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import edu.java.dto.QuestionInformation;
import edu.java.utils.TimeConvertor;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;

@Validated
public class StackOverflowClient extends HttpClient {
    private static final String DEFAULT_URL = "https://api.stackexchange.com";
    private static final String START_PATH = "/questions";

    public StackOverflowClient(HttpHeaders headers) {
        this(DEFAULT_URL, headers);
    }

    public StackOverflowClient(String baseUrl, HttpHeaders headers) {
        super(baseUrl, headers);
    }

    @SuppressWarnings("MultipleStringLiterals")
    public Optional<QuestionInformation> getInformationAboutQuestion(long questionId) throws JsonProcessingException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("site", "stackoverflow");
        String response;
        try {
            response = getResponse(
                String.join("/", START_PATH, Long.toString(questionId)),
                params,
                "Not found question"
            );
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
        JsonNode node = objectMapper.readTree(response).get("items").get(0);
        QuestionInformation questionInformation = objectMapper.treeToValue(node, QuestionInformation.class);
        Optional<QuestionInformation.AdditionalInformation> lastComment =
            getAdditionalInformationAboutQuestion(questionId, AdditionalInformation.COMMENT);
        Optional<QuestionInformation.AdditionalInformation> lastAnswer =
            getAdditionalInformationAboutQuestion(questionId, AdditionalInformation.ANSWER);
        lastComment.ifPresent(questionInformation::setLastComment);
        lastAnswer.ifPresent(questionInformation::setLastAnswer);
        return Optional.of(questionInformation);
    }

    private Optional<QuestionInformation.AdditionalInformation> getLastCommentForQuestion(long questionId)
        throws JsonProcessingException {
        return getAdditionalInformationAboutQuestion(questionId, AdditionalInformation.COMMENT);
    }

    private Optional<QuestionInformation.AdditionalInformation> getAdditionalInformationAboutQuestion(
        long questionId,
        AdditionalInformation additionalInfType
    ) throws JsonProcessingException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("pagesize", "1");
        params.add("order", "desc");
        params.add("sort", "creation");
        params.add("site", "stackoverflow");
        params.add("filter", additionalInfType.filter);

        String response;
        try {
            response = getResponse(
                String.join("/", START_PATH, Long.toString(questionId), additionalInfType.pathName),
                params,
                String.format("Not found %s for question", additionalInfType.pathName)
            );
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
        JsonNode node = objectMapper.readTree(response).get("items").get(0);
        OffsetDateTime creationDate = TimeConvertor.convertEpochToOffsetDateTime(node.get("creation_date").asLong());
        String text = node.get("body").asText();
        String link = node.get("link").asText();
        String ownerName = node.get("owner").get("display_name").asText();
        QuestionInformation.AdditionalInformation
            inf = new QuestionInformation.AdditionalInformation(creationDate, ownerName, text, link);
        return Optional.of(inf);
    }

    private enum AdditionalInformation {
        ANSWER("answers", "!6WPIomp-eb(U5"),
        COMMENT("comments", "!6WPIompltQw.p");
        public final String pathName;
        public final String filter;

        AdditionalInformation(String pathName, String filter) {
            this.pathName = pathName;
            this.filter = filter;
        }
    }
}
