package edu.java.request;

import java.util.List;
import lombok.Value;
import wiremock.com.fasterxml.jackson.annotation.JsonProperty;

public record LinkUpdateRequest(String url,  String description,
                                 List<Long> tgChatsId) {
}
