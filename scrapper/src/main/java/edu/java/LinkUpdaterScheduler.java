package edu.java;

import edu.java.client.BotClient;
import edu.java.dao.dto.LinkAndChatDTO;
import edu.java.dao.dto.LinkDTO;
import edu.java.dao.service.ChatService;
import edu.java.dao.service.LinkService;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class LinkUpdaterScheduler {
    private static final Duration TIME_DURATION_TO_CHECK_LINK = Duration.ofDays(1);
    private static final String UPDATE_MESSAGE = " Link get update";
    private final BotClient botClient;
    private final LinkService linkService;
    private final ChatService chatService;

    @Autowired
    public LinkUpdaterScheduler(BotClient botClient, LinkService linkService, ChatService chatService) {
        this.botClient = botClient;
        this.linkService = linkService;
        this.chatService = chatService;
    }

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        for (LinkDTO link : linkService.listAllOutdated(TIME_DURATION_TO_CHECK_LINK)) {
            List<Long> chatsId =
                chatService.getChatsThatTrackLink(link.getUrl()).stream().map(LinkAndChatDTO::getChatId).toList();
            botClient.sendUpdate(chatsId, link.getUrl(), UPDATE_MESSAGE);
        }
    }
}
