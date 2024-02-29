package edu.java.dto.response;

import lombok.Data;
import lombok.Value;
import java.net.URI;

@Value
public class LinkResponse {
    Long id;
    URI url;
}
