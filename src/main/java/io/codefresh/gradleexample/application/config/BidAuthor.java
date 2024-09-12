package io.codefresh.gradleexample.application.config;

import java.util.Arrays;

public enum BidAuthor {
    USER("User"),
    ORGANIZATION("Organization");

    private final String value;

    BidAuthor(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        return Arrays.stream(BidAuthor.values()).anyMatch(t -> t.value.equalsIgnoreCase(value));
    }
}
