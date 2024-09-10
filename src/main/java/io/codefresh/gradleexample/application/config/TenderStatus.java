package io.codefresh.gradleexample.application.config;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TenderStatus {
    CREATED("CREATED"),
    PUBLISHED("PUBLISHED"),
    CLOSED("CLOSED");

    private final String value;

    TenderStatus(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        return Arrays.stream(TenderStatus.values()).anyMatch(t -> t.value.equalsIgnoreCase(value));
    }
}
