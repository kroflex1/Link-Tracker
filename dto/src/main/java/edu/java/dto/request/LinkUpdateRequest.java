package edu.java.dto.request;

import java.util.List;

public record LinkUpdateRequest(String url, String description, List<Long> tgChatsId) {
}
