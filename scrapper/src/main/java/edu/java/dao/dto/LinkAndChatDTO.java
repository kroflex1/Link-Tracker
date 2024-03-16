package edu.java.dao.dto;

import lombok.Value;
import java.net.URI;

@Value
public class LinkAndChatDTO {
    Long id;
    URI link;
    Long chatId;
}
