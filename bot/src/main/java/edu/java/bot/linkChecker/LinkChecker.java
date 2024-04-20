package edu.java.bot.linkChecker;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class LinkChecker {
    private LinkChecker nextLinkChecker;

    public void setNextUrlHandler(LinkChecker urlHandler) {
        this.nextLinkChecker = urlHandler;
    }

    public LinkChecker getNextUrlHandler() {
        return nextLinkChecker;
    }

    public boolean isValidLink(String link) {
        try {
            URL url = new URL(link);
            if (checkUrl(url)) {
                return true;
            } else if (nextLinkChecker != null) {
                return nextLinkChecker.isValidLink(link);
            }
        } catch (MalformedURLException e) {
            return false;
        }
        return false;
    }

    public abstract String linkFormatDescription();

    protected abstract boolean checkUrl(URL url);
}
