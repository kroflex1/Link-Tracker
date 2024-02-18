package edu.java.bot.handler;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubHandler extends UrlHandler {
    @Override
    protected boolean checkUrl(URL url) {
        Pattern pattern = Pattern.compile("https://github\\.com/[a-zA-Z0-9-]+/[a-zA-Z0-9-]+/?");
        Matcher matcher = pattern.matcher(url.toString());
        return matcher.matches();
    }

    @Override
    public String formatDescription() {
        return "GitHub:\n\thttps://github.com/<username>/<repository>";
    }
}
