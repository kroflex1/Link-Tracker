package edu.java.bot.handler;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class UrlHandler {
    private UrlHandler nextUrlHandler;

    public void setNextUrlHandler(UrlHandler urlHandler) {
        this.nextUrlHandler = urlHandler;
    }

    public UrlHandler getNextUrlHandler() {
        return nextUrlHandler;
    }

    public boolean isValidLink(String link) {
        try {
            URL url = new URL(link);
            if (checkUrl(url)) {
                return true;
            } else if (nextUrlHandler != null) {
                return nextUrlHandler.isValidLink(link);
            }
        } catch (MalformedURLException e) {
            return false;
        }
        return false;
    }

    public abstract String formatDescription();

    protected abstract boolean checkUrl(URL url);
}
