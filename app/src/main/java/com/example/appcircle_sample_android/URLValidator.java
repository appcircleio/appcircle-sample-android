package com.example.appcircle_sample_android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLValidator {
    private static final String OWASP_URL_REGEX =
            "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
                    "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
                    "([).!';/?:,][[:blank:]])?$";

    private static final Pattern URL_PATTERN = Pattern.compile(OWASP_URL_REGEX);

    public static boolean isValid(String url) {
        if (url == null) {
            return false;
        }

        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }
}
