package edu.java.dto.response;

import java.util.List;

public record ListLinksResponse(Integer size, List<LinkResponse> links) {
}
