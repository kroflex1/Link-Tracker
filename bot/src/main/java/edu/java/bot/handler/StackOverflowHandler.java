package edu.java.bot.handler;

import java.net.URL;

public class StackOverflowHandler extends UrlHandler {
    @Override
    protected boolean checkUrl(URL url) {
        if (!url.getProtocol().equals("https")) {
            return false;
        }
        if (!url.getHost().equals("stackoverflow.com")) {
            return false;
        }
        return !url.getPath().isEmpty();
    }

    @Override
    public String formatDescription() {
        return "StackOverflowHandler: https://stackoverflow.com/<parameters>";
    }
}
