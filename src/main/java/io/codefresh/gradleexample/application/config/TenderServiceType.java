package io.codefresh.gradleexample.application.config;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TenderServiceType {
    CONSTRUCTION("Construction"),
    DELIVERY("Delivery"),
    MANUFACTURE("Manufacture");

    private final String value;

    TenderServiceType(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        return Arrays.stream(TenderServiceType.values())
                .anyMatch(t -> t.value.equalsIgnoreCase(value));
    }
}
