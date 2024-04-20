package edu.java.scrapper.dao.service.jpa;

import edu.java.dao.dto.LinkDTO;
import edu.java.dao.repository.jpa.JpaChatRepository;
import edu.java.dao.repository.jpa.JpaLinkRepository;
import edu.java.dao.service.jpa.JpaLinkService;
import edu.java.exceptions.AlreadyTrackedLinkException;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class JpaLinkServiceTest extends IntegrationTest {
    @Autowired JpaChatRepository jpaChatRepository;
    @Autowired JpaLinkRepository jpaLinkRepository;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Rollback
    void testTrackLink() {
        JpaLinkService jpaLinkService = new JpaLinkService(jpaLinkRepository, jpaChatRepository);
        URI link = URI.create("http://somelink");
        long tgChatId = 1L;

        jpaLinkService.startTrackLink(tgChatId, link);
        LinkDTO actual = jpaLinkService.getAllTrackedLinksByChat(tgChatId).stream().toList().getFirst();

        assertEquals(link, actual.getUrl());
    }

    @Test
    @Rollback
    void testStartTrackAlreadyTrackedLink() {
        JpaLinkService jpaLinkService = new JpaLinkService(jpaLinkRepository, jpaChatRepository);
        URI link = URI.create("http://somelink");
        Long tgChatId = 1L;

        jpaLinkService.startTrackLink(tgChatId, link);

        Exception exception = assertThrows(AlreadyTrackedLinkException.class, () ->
            jpaLinkService.startTrackLink(tgChatId, link));
        assertEquals("Chat with id=%d already tracking link %s".formatted(tgChatId, link), exception.getMessage());
    }

    @Test
    @Rollback
    void testTrackManyLinks() {
        JpaLinkService jpaLinkService = new JpaLinkService(jpaLinkRepository, jpaChatRepository);
        int numberOfLinks = 5;
        Long tgChatId = 1L;
        Set<URI> expected = new HashSet<>();

        for (int i = 0; i < numberOfLinks; i++) {
            URI link = URI.create("http://somelink" + i);
            expected.add(link);
            jpaLinkService.startTrackLink(tgChatId, link);
        }
        Set<URI> actual = jpaLinkService.getAllTrackedLinksByChat(tgChatId)
            .stream()
            .map(LinkDTO::getUrl)
            .collect(Collectors.toSet());

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("trackedLinksByChats")
    @Rollback
    void testManyChatsTrackLinks(Map<Long, Set<URI>> trackedLinks) {
        JpaLinkService jpaLinkService = new JpaLinkService(jpaLinkRepository, jpaChatRepository);
        Map<Long, Set<URI>> actual = new HashMap<>();

        for (Map.Entry<Long, Set<URI>> record : trackedLinks.entrySet()) {
            for (URI link : record.getValue()) {
                jpaLinkService.startTrackLink(record.getKey(), link);
            }
        }
        for (Long chatId : trackedLinks.keySet()) {
            Set<URI> actualTrackedLinks = jpaLinkService
                .getAllTrackedLinksByChat(chatId)
                .stream()
                .map(LinkDTO::getUrl)
                .collect(Collectors.toSet());
            actual.put(chatId, actualTrackedLinks);
        }

        assertEquals(trackedLinks, actual);
    }

    @Test
    @Rollback
    void testStopTrackLink() {
        JpaLinkService jpaLinkService = new JpaLinkService(jpaLinkRepository, jpaChatRepository);
        URI link = URI.create("http://somelink");
        long tgChatId = 1L;

        jpaLinkService.startTrackLink(tgChatId, link);
        jpaLinkService.stopTrackLink(tgChatId, link);

        assertTrue(jpaLinkService.getAllTrackedLinksByChat(tgChatId).isEmpty());
    }

    @Test
    @Rollback
    void testUpdateLastCheckTime() {
        JpaLinkService jpaLinkService = new JpaLinkService(jpaLinkRepository, jpaChatRepository);
        URI link = URI.create("http://somelink");
        OffsetDateTime expectedLastCheckTime = OffsetDateTime.now().plusHours(1);

        jpaLinkService.startTrackLink(1L, link);
        jpaLinkService.updateLastCheckTime(link, expectedLastCheckTime);
        OffsetDateTime actual =
            jpaLinkService.getAllTrackedLinksByChat(1L).stream().toList().getFirst().getLastCheckTime();

        assertEquals(expectedLastCheckTime, actual);
    }

    @Test
    @Rollback
    void testUpdateLastActivityTime() {
        JpaLinkService jpaLinkService = new JpaLinkService(jpaLinkRepository, jpaChatRepository);
        URI link = URI.create("http://somelink");
        OffsetDateTime expectedLastActivityTime = OffsetDateTime.now().plusHours(1);

        jpaLinkService.startTrackLink(1L, link);
        jpaLinkService.updateLastActivityTime(link, expectedLastActivityTime);
        OffsetDateTime actual =
            jpaLinkService.getAllTrackedLinksByChat(1L).stream().toList().getFirst().getLastActivityTime();

        assertEquals(expectedLastActivityTime, actual);
    }

    private static Stream<Arguments> trackedLinksByChats() {
        Map<Long, Set<URI>> firstTrackedLinks = new HashMap<>() {{
            put(1L, Set.of(URI.create("http://link")));
            put(2L, Set.of(URI.create("http://link")));
        }};
        Map<Long, Set<URI>> secondTrackedLinks = new HashMap<>() {{
            put(1L, Set.of(URI.create("http://link1"), URI.create("http://link2")));
            put(2L, Set.of(URI.create("http://link3")));
        }};
        Map<Long, Set<URI>> thirdTrackedLinks = new HashMap<>() {{
            put(1L, Set.of(URI.create("http://link1"), URI.create("http://link2")));
            put(2L, Set.of(URI.create("http://link1")));
        }};
        return Stream.of(
            Arguments.of(firstTrackedLinks),
            Arguments.of(secondTrackedLinks),
            Arguments.of(thirdTrackedLinks)
        );
    }

}
