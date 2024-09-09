package io.codefresh.gradleexample.application.config;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ServiceType {
    CONSTRUCTOR("Construction"),
    DELIVERY("Delivery"),
    MANUFACTURE("Manufacture");

    private final String value;

    ServiceType(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        return Arrays.stream(ServiceType.values()).anyMatch(t -> t.value.equals(value));
    }
}
