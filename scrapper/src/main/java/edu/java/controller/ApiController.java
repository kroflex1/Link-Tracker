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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    Map<Long, Set<URI>> chatsInf = new HashMap<>();

    @GetMapping("/tg-chat/{id}")
    @Operation(summary = "Register chat")
    public ResponseEntity registerChat(@PathVariable Long id) {
        if (chatsInf.containsKey(id)) {
            throw new IllegalArgumentException("Chat with this id has already been registered");
        }
        chatsInf.put(id, new HashSet<>());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tg-chat/{id}")
    @Operation(summary = "Remove chat")
    public ResponseEntity removeChat(@PathVariable Long id) {
        if (!chatsInf.containsKey(id)) {
            throw new IllegalArgumentException("Chat with this id wasn`t found");
        }
        chatsInf.remove(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/links")
    @Operation(summary = "Get all tracked links")
    public ListLinksResponse getLinks(@RequestHeader Long chatId) {
        if (!chatsInf.containsKey(chatId)) {
            throw new IllegalArgumentException("Chat with this id wasn`t found");
        }
        List<LinkResponse> trackedLinks = new ArrayList<>();
        for (URI link : chatsInf.get(chatId)) {
            trackedLinks.add(new LinkResponse(chatId, link));
        }
        return new ListLinksResponse(trackedLinks.size(), trackedLinks);
    }

    @PostMapping("/links")
    @Operation(summary = "Add tracked link")
    public LinkResponse addLinks(@RequestHeader Long chatId, @RequestBody AddLinkRequest linkInf)
        throws MalformedURLException, URISyntaxException {
        if (!chatsInf.containsKey(chatId)) {
            throw new IllegalArgumentException("Chat with this id wasn`t found");
        }
        URI uri = new URL(linkInf.link()).toURI();
        if (chatsInf.get(chatId).contains(uri)) {
            throw new IllegalArgumentException("This link is already register");
        }
        chatsInf.get(chatId).add(uri);
        return new LinkResponse(chatId, uri);
    }

    @DeleteMapping("/links")
    @Operation(summary = "Remove tracked link")
    public RemoveLinkResponse removeLinks(@RequestHeader Long chatId, @RequestBody AddLinkRequest linkInf) {
        if (!chatsInf.containsKey(chatId)) {
            throw new IllegalArgumentException("Chat with this id wasn`t found");
        }
        return new RemoveLinkResponse(URI.create(linkInf.link()));
    }
}
