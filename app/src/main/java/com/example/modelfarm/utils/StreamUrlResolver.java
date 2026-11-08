package com.example.modelfarm.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

/**
 * Validates the complete RTMP URL stored in a camera's pushName field.
 */
public final class StreamUrlResolver {

    private StreamUrlResolver() {
    }

    public static String resolve(String pushName) {
        if (pushName == null) {
            return null;
        }

        String candidate = pushName.trim();
        if (candidate.isEmpty()) {
            return null;
        }

        try {
            URI uri = new URI(candidate);
            String scheme = uri.getScheme();
            String normalizedScheme = scheme == null ? "" : scheme.toLowerCase(Locale.ROOT);
            boolean supportedScheme = "rtmp".equals(normalizedScheme) || "rtmps".equals(normalizedScheme);
            boolean hasHost = uri.getHost() != null && !uri.getHost().isEmpty();
            boolean hasStreamPath = uri.getPath() != null && uri.getPath().split("/").length >= 3;
            boolean hasValidPort = uri.getPort() == -1 || (uri.getPort() > 0 && uri.getPort() <= 65535);

            return supportedScheme && hasHost && hasStreamPath && hasValidPort ? candidate : null;
        } catch (URISyntaxException exception) {
            return null;
        }
    }
}
