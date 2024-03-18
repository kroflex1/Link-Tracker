package edu.java;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.client.BotClient;
import edu.java.client.GitHubClient;
import edu.java.client.StackOverflowClient;
import edu.java.client.dto.QuestionInformation;
import edu.java.client.dto.RepositoryInformation;
import edu.java.dao.dto.LinkAndChatDTO;
import edu.java.dao.dto.LinkDTO;
import edu.java.dao.service.ChatService;
import edu.java.dao.service.LinkService;
import java.net.URI;
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
    private static final String UPDATE_MESSAGE = " Link get update";
    private final BotClient botClient;
    private final StackOverflowClient stackOverflowClient;
    private final GitHubClient gitHubClient;
    private final LinkService linkService;
    private final ChatService chatService;

    @Autowired
    public LinkUpdaterScheduler(
        BotClient botClient,
        LinkService linkService,
        ChatService chatService,
        StackOverflowClient stackOverflowClient,
        GitHubClient gitHubClient
    ) {
        this.botClient = botClient;
        this.linkService = linkService;
        this.chatService = chatService;
        this.gitHubClient = gitHubClient;
        this.stackOverflowClient = stackOverflowClient;
    }

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        for (LinkDTO link : linkService.listAllOutdated(TIME_DURATION_TO_CHECK_LINK)) {
            if (hasLinkBeenUpdated(link.getUrl(), link.getLastActivityTime())) {
                List<Long> chatsId =
                    chatService.getChatsThatTrackLink(link.getUrl()).stream().map(LinkAndChatDTO::getChatId).toList();
                botClient.sendUpdate(chatsId, link.getUrl(), UPDATE_MESSAGE);
            }
        }
    }

    private boolean hasLinkBeenUpdated(URI url, OffsetDateTime lastActivityTime) {
        Optional<RepositoryInformation> gitHubActivity = gitHubClient.getRepositoryInformation(url);
        Optional<QuestionInformation> stackOverflowActivity;
        try {
            stackOverflowActivity = stackOverflowClient.getInformationAboutQuestion(url);
        } catch (JsonProcessingException e) {
            stackOverflowActivity = Optional.empty();
        }
        if (gitHubActivity.isPresent() && gitHubActivity.get().getLastUpdateTime().isAfter(lastActivityTime)) {
            return true;
        }
        if (stackOverflowActivity.isPresent() &&
            stackOverflowActivity.get().getLastUpdateTime().isAfter(lastActivityTime)) {
            return true;
        }
        return false;
    }
}
