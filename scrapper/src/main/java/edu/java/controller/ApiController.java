package edu.java.controller;

import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinksResponse;
import edu.java.dto.response.RemoveLinkResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    Map<Long, Set<URI>> chatsInf = new HashMap<>();

    @PostMapping("/tg-chat/{id}")
    @Operation(summary = "Register chat")
    public ResponseEntity registerChat(@PathVariable Long id) {
        if (chatsInf.containsKey(id)) {
            throw new IllegalArgumentException("Chat with this id already registered");
        }
        chatsInf.put(id, new HashSet<>());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tg-chat/{id}")
    @Operation(summary = "Remove chat")
    public ResponseEntity removeChat(@PathVariable Long id) {
        checkForChatId(id);
        chatsInf.remove(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/links/{chatId}")
    @Operation(summary = "Get all tracked links")
    public ListLinksResponse getLinks(@PathVariable Long chatId) {
        checkForChatId(chatId);
        List<LinkResponse> trackedLinks = new ArrayList<>();
        for (URI link : chatsInf.get(chatId)) {
            trackedLinks.add(new LinkResponse(chatId, link));
        }
        return new ListLinksResponse(trackedLinks.size(), trackedLinks);
    }

    @PostMapping("/links/{chatId}")
    @Operation(summary = "Add tracked link")
    public LinkResponse addLinks(@PathVariable Long chatId, @RequestBody AddLinkRequest linkInf)
        throws MalformedURLException, URISyntaxException {
        checkForChatId(chatId);
        URI uri = new URL(linkInf.link()).toURI();
        if (chatsInf.get(chatId).contains(uri)) {
            throw new IllegalArgumentException("This link is already register");
        }
        chatsInf.get(chatId).add(uri);
        return new LinkResponse(chatId, uri);
    }

    @DeleteMapping("/links/{chatId}")
    @Operation(summary = "Remove tracked link")
    public RemoveLinkResponse removeLinks(@PathVariable Long chatId, @RequestBody AddLinkRequest linkInf) {
        checkForChatId(chatId);
        if (!chatsInf.get(chatId).contains(URI.create(linkInf.link()))) {
            throw new IllegalArgumentException("Link was not found");
        }
        return new RemoveLinkResponse(URI.create(linkInf.link()));
    }

    private void checkForChatId(Long chatId) throws IllegalArgumentException {
        if (!chatsInf.containsKey(chatId)) {
            throw new IllegalArgumentException("Chat with this id wasn`t found");
        }
    }
}
