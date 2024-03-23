package edu.java;

import edu.java.client.BotClient;
import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import edu.java.dao.dto.LinkAndChatDTO;
import edu.java.dao.dto.LinkDTO;
import edu.java.dao.service.ChatService;
import edu.java.dao.service.LinkService;
import edu.java.utils.linkInformant.GithubLinkInformant;
import edu.java.utils.linkInformant.LinkInformant;
import edu.java.utils.linkInformant.StackOverflowInformant;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class LinkUpdaterScheduler {
    private static final Duration TIME_DURATION_TO_CHECK_LINK = Duration.ofDays(1);
    private final LinkService linkService;
    private final ChatService chatService;
    private final BotClient botClient;

    private final LinkInformant linkInformant;

    @Autowired
    public LinkUpdaterScheduler(
        BotClient botClient,
        StackOverflowClient stackOverflowClient,
        GitHubClient gitHubClient,
        LinkService linkService,
        ChatService chatService
    ) {
        this.botClient = botClient;
        this.linkService = linkService;
        this.chatService = chatService;

        linkInformant = new StackOverflowInformant(stackOverflowClient);
        linkInformant.setNextLinkUpdateDescription(new GithubLinkInformant(gitHubClient));
    }

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        for (LinkDTO link : linkService.listAllOutdated(TIME_DURATION_TO_CHECK_LINK)) {
            linkService.updateLastCheckTime(link.getUrl(), OffsetDateTime.now());

            Optional<LinkInformant.LinkActivityInformation> activityInformation =
                linkInformant.getLinkActivityInformationAfterDate(link.getLastActivityTime(), link.getUrl());
            if (activityInformation.isPresent()) {
                linkService.updateLastActivityTime(link.getUrl(), activityInformation.get().lastActivityTime());
                List<Long> chatsId =
                    chatService.getChatsThatTrackLink(link.getUrl()).stream().map(LinkAndChatDTO::getChatId).toList();
                botClient.sendUpdate(chatsId, link.getUrl(), activityInformation.get().message());
            }
        }
    }
}
