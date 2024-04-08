package edu.java.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import edu.java.client.inforamation.QuestionInformation;
import edu.java.client.retry.RetryPolicy;
import edu.java.utils.TimeManager;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import reactor.util.retry.Retry;

@Validated
public class StackOverflowClient extends HttpClient {
    private static final String DEFAULT_URL = "https://api.stackexchange.com";
    private static final String START_PATH = "/questions";
    private static final Pattern PATTERN_FOR_LINK = Pattern.compile("https://stackoverflow\\.com/questions/(\\d+)/.+");
    private static final Retry DEFAULT_RETRY_POLICY = RetryPolicy.CONSTANT.getRetry(2, Duration.ofSeconds(2));
    private static final Set<HttpStatusCode> codesForRetry =
        Set.of(HttpStatusCode.valueOf(500), HttpStatusCode.valueOf(501));

    public StackOverflowClient(HttpHeaders headers) {
        this(DEFAULT_URL, headers, DEFAULT_RETRY_POLICY);
    }

    public StackOverflowClient(String baseUrl, HttpHeaders headers) {
        this(baseUrl, headers, DEFAULT_RETRY_POLICY);
    }

    public StackOverflowClient(String baseUrl, HttpHeaders headers, Retry retryPolicy) {
        super(baseUrl, headers, retryPolicy);
    }

    public Optional<QuestionInformation> getInformationAboutQuestion(URI url) {
        Matcher matcher = PATTERN_FOR_LINK.matcher(url.toString());
        if (matcher.matches()) {
            return getInformationAboutQuestion(Long.parseLong(matcher.group(1)));
        }
        return Optional.empty();
    }

    @SuppressWarnings("MultipleStringLiterals")
    public Optional<QuestionInformation> getInformationAboutQuestion(long questionId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("site", "stackoverflow");
        String response;
        try {
            response = getResponse(
                String.join("/", START_PATH, Long.toString(questionId)),
                params,
                "Not found question",
                codesForRetry
            );
        } catch (IllegalArgumentException | ServiceException e) {
            return Optional.empty();
        }
        JsonNode node;
        QuestionInformation questionInformation;
        try {
            node = objectMapper.readTree(response).get("items").get(0);
            questionInformation = objectMapper.treeToValue(node, QuestionInformation.class);
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }

        Optional<QuestionInformation.AdditionalInformation> lastComment =
            getAdditionalInformationAboutQuestion(questionId, AdditionalInformation.COMMENT);
        Optional<QuestionInformation.AdditionalInformation> lastAnswer =
            getAdditionalInformationAboutQuestion(questionId, AdditionalInformation.ANSWER);

        lastComment.ifPresent(questionInformation::setLastComment);
        lastAnswer.ifPresent(questionInformation::setLastAnswer);
        return Optional.of(questionInformation);
    }

    private Optional<QuestionInformation.AdditionalInformation> getAdditionalInformationAboutQuestion(
        long questionId,
        AdditionalInformation additionalInfType
    ) {
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
                String.format("Not found %s for question", additionalInfType.pathName),
                codesForRetry
            );
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }

        JsonNode node;
        try {
            node = objectMapper.readTree(response).get("items").get(0);
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
        if (node == null) {
            return Optional.empty();
        }
        OffsetDateTime creationDate = TimeManager.convertEpochToOffsetDateTime(node.get("creation_date").asLong());
        String text = node.get("body").asText();
        String link = node.get("link").asText();
        String ownerName = node.get("owner").get("display_name").asText();
        QuestionInformation.AdditionalInformation
            additionalInf = new QuestionInformation.AdditionalInformation(creationDate, ownerName, text, link);
        return Optional.of(additionalInf);
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
