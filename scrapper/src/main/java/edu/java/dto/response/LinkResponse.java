package edu.java.dto.response;

import java.net.URI;
import lombok.Value;

@Value
public class LinkResponse {
    Long id;
    URI url;
}
