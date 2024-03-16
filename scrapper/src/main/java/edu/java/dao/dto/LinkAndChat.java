package edu.java.dao.dto;

import java.net.URI;
import lombok.Value;

@Value
public class LinkAndChat {
    Long id;
    URI link;
    Long chatId;
}
