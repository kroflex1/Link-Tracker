package edu.java.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.dto.QuestionInformation;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Validated
public class StackOverflowClient {
    private static final String DEFAULT_URL = "https://api.stackexchange.com";
    private static final String NOT_FOUND_QUESTION_MESSAGE = "Question was not found";
    private final WebClient webClient;
    private final String baseUrl;
    private final ObjectMapper objectMapper;

    public StackOverflowClient() {
        this(DEFAULT_URL);
    }

    public StackOverflowClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.findAndRegisterModules();
    }

//    public QuestionInformation getInformationAboutQuestion(@NotNull Long questionId){
//
//    }

//    private QuestionInformation.Comment getLastCommentForQuestion(@NotNull Long questionId) {
//
//    }

    private String getResponse(String uri, String params) {
        return webClient
            .get()
            .uri(uri)
            .retrieve()
            .onStatus(
                status -> status == HttpStatus.NOT_FOUND,
                clientResponse -> Mono.error(new IllegalArgumentException(NOT_FOUND_QUESTION_MESSAGE))
            )
            .bodyToMono(String.class)
            .block();
    }
}
