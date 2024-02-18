package edu.java.bot.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

public class UserModel {
    private final Long id;
    @Getter private final Set<String> links;

    public UserModel(Long id) {
        this.id = id;
        this.links = new HashSet<>();
    }

    public void addLink(String url) {
        links.add(url);
    }

    public void removeLink(String url) {
        links.remove(url);
    }
}
