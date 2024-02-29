package edu.java.bot.dto.request;

import java.net.URI;
import java.util.List;
import lombok.Data;

@Data
public class LinkUpdateRequest {
    private Long id;
    private URI url;
    private String description;
    private List<Long> tgChatIds;
}
