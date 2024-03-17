package edu.java.controller;

import edu.java.dao.dto.LinkDTO;
import edu.java.dao.service.ChatService;
import edu.java.dao.service.LinkService;
import edu.java.request.AddLinkRequest;
import edu.java.response.LinkResponse;
import edu.java.response.ListLinksResponse;
import edu.java.response.RemoveLinkResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    private final ChatService chatService;
    private final LinkService linkService;

    @Autowired
    public ApiController(ChatService chatService, LinkService linkService) {
        this.chatService = chatService;
        this.linkService = linkService;
    }

    @PostMapping("/tg-chat/{id}")
    @Operation(summary = "Register chat")
    public ResponseEntity registerChat(@PathVariable Long id) throws IllegalArgumentException {
        chatService.register(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tg-chat/{id}")
    @Operation(summary = "Remove chat")
    public ResponseEntity removeChat(@PathVariable Long id) throws IllegalArgumentException {
        chatService.unregister(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/links/{chatId}")
    @Operation(summary = "Get all tracked links")
    public ListLinksResponse getLinks(@PathVariable Long chatId) {
        List<LinkResponse> trackedLinks = new ArrayList<>();
        for (LinkDTO link : linkService.listAll(chatId)) {
            trackedLinks.add(new LinkResponse(chatId, link.getUrl()));
        }
        return new ListLinksResponse(trackedLinks.size(), trackedLinks);
    }

    @PostMapping("/links/{chatId}")
    @Operation(summary = "Add tracked link")
    public LinkResponse addLinks(@PathVariable Long chatId, @RequestBody AddLinkRequest linkInf)
        throws IllegalArgumentException, MalformedURLException, URISyntaxException {
        URI uri = new URL(linkInf.link()).toURI();
        linkService.add(chatId, uri);
        return new LinkResponse(chatId, uri);
    }

    @DeleteMapping("/links/{chatId}")
    @Operation(summary = "Remove tracked link")
    public RemoveLinkResponse removeLinks(@PathVariable Long chatId, @RequestBody AddLinkRequest linkInf)
        throws MalformedURLException, URISyntaxException {
        URI uri = new URL(linkInf.link()).toURI();
        linkService.remove(chatId, uri);
        return new RemoveLinkResponse(URI.create(linkInf.link()));
    }

}
