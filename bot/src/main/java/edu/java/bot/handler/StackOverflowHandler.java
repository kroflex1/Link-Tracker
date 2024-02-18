package edu.java.bot.handler;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackOverflowHandler extends UrlHandler {
    @Override
    protected boolean checkUrl(URL url) {
        Pattern searchPattern = Pattern.compile("https://stackoverflow\\.com/search\\?q=[a-zA-Z0-9%]+");
        Pattern questionPattern = Pattern.compile("https://stackoverflow\\.com/questions/\\d+(/.+)?");
        Matcher searchMatcher = searchPattern.matcher(url.toString());
        Matcher questionMatcher = questionPattern.matcher(url.toString());
        return searchMatcher.matches() || questionMatcher.matches();
    }

    @Override
    public String formatDescription() {
        return "StackOverflowHandler:\n\thttps://stackoverflow.com/search?q=<param>\n\thttps://stackoverflow.com/questions/<param>/<param>";
    }
}
