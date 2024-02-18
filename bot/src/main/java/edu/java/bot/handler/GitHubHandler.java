package edu.java.bot.handler;

import java.net.URL;

public class GitHubHandler extends UrlHandler {
    @Override
    protected boolean checkUrl(URL url) {
        if (!url.getProtocol().equals("https")) {
            return false;
        }
        if (!url.getHost().equals("github.com")) {
            return false;
        }
        return !url.getPath().isEmpty();
    }

    @Override
    public String formatDescription() {
        return "GitHub: https://github.com/<parameters>";
    }
}
