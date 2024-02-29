package edu.java.dto.response;

import lombok.Data;
import lombok.Value;
import java.util.List;

@Value
public class ListLinksResponse {
    Integer size;
    List<LinkResponse> links;
}
