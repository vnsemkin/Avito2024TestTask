package io.codefresh.gradleexample.application.dtos;

import io.codefresh.gradleexample.application.config.TenderServiceType;

import java.util.Arrays;

public record TenderEditReq(String name,
                            String description,
                            String serviceType) {
    private final static String INVALID_SERVICE_TYPE = "Invalid serviceType: should be one of %s";

    public TenderEditReq {
        if (!TenderServiceType.contains(serviceType)) {
            throw new IllegalArgumentException(String.format(INVALID_SERVICE_TYPE,
                    Arrays.stream(TenderServiceType.values())
                            .map(TenderServiceType::getValue).toList()));
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Name must be less than 100 characters.");
        }
        if (description.length() > 500) {
            throw new IllegalArgumentException("Description must be less than 500 characters.");
        }
    }
}
