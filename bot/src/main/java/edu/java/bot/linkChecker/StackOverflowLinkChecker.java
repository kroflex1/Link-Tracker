package edu.java.bot.linkChecker;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackOverflowLinkChecker extends LinkChecker {
    @Override
    protected boolean checkUrl(URL url) {
        Pattern pattern = Pattern.compile("https://stackoverflow\\.com/questions/\\d+(/.+)?");
        Matcher matcher = pattern.matcher(url.toString());
        return matcher.matches();
    }

    @Override
    public String linkFormatDescription() {
        return "StackOverflow:\n\thttps://stackoverflow.com/questions/<question_id>/<question_title>";
    }
}
