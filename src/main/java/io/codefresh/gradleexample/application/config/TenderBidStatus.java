package io.codefresh.gradleexample.application.config;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TenderBidStatus {
    CREATED("Created"),
    PUBLISHED("Published"),
    CLOSED("Closed");

    private final String value;

    TenderBidStatus(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        return Arrays.stream(TenderBidStatus.values()).anyMatch(t -> t.value.equalsIgnoreCase(value));
    }
}
