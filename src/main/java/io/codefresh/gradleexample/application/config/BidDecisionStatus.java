package io.codefresh.gradleexample.application.config;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BidDecisionStatus {
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String value;

    BidDecisionStatus(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        return Arrays.stream(BidDecisionStatus.values()).anyMatch(t -> t.value.equalsIgnoreCase(value));
    }
}
