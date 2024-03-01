package edu.java.dto.response;

import java.util.List;
import lombok.Value;

@Value
public class ListLinksResponse {
    Integer size;
    List<LinkResponse> links;
}
