package com.yolt.providers.common.util;


import com.yolt.providers.common.exception.InvalidUrlException;

import java.util.Locale;
import java.util.Optional;

public class PinUrlHelper {
    public static final String HTTPS_PREFIX = "https://";

    private PinUrlHelper() {
    }

    public static boolean containsHttpsUrl(String... urls) {
        for (String url : urls) {
            if (url != null && url.startsWith(HTTPS_PREFIX)) {
                return true;
            }
        }
        return false;
    }

    public static Optional<String> getPinUrlIfHttps(final String url) {
        try {
            validateUrl(url);
        } catch (InvalidUrlException e) { //NOSONAR No need to log or rethrow exception. We return Optional.empty().
            return Optional.empty();
        }

        String pinUrl = removeHttpsFromUrl(url);
        pinUrl = removePartAfterFirstForwardSlash(pinUrl);

        return Optional.of(pinUrl);
    }

    public static String getPinUrlForHttpsUrl(final String url) {
        validateUrl(url);

        String pinUrl = removeHttpsFromUrl(url);
        pinUrl = removePartAfterFirstForwardSlash(pinUrl);

        return pinUrl;
    }

    private static void validateUrl(final String url) {
        if (url == null) {
            throw new InvalidUrlException("Cannot extract pin URL from URL that is null");
        }

        //Locale.ENGLISH is due to Fortify complaining otherwise
        String urlLowercase = url.toLowerCase(Locale.ENGLISH);

        if (!urlLowercase.startsWith(HTTPS_PREFIX)) {
            throw new InvalidUrlException("Cannot extract pin URL from non-https URL");
        }
    }

    private static String removeHttpsFromUrl(final String url) {
        int httpsLength = HTTPS_PREFIX.length();
        return url.substring(httpsLength, url.length());
    }

    private static String removePartAfterFirstForwardSlash(String url) {
        if (!url.contains("/")) {
            return url;
        }

        int index = url.indexOf('/');
        return url.substring(0, index);
    }
}
