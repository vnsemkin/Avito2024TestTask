package io.codefresh.gradleexample.application.dtos;

import org.springframework.lang.NonNull;

import java.util.UUID;

public record TenderStatusReq(
        @NonNull String tenderId,
        @NonNull String username) {
    private static final String INVALID_USERNAME = "Username must be less than 50 characters.";
    private static final String INVALID_TENDER_ID = "Invalid tenderId: should be UUID";

    public TenderStatusReq {
        if (!isUuid(tenderId)) {
            throw new IllegalArgumentException(INVALID_TENDER_ID);
        }
        if (username.length() > 50) {
            throw new IllegalArgumentException(INVALID_USERNAME);
        }
    }

    private boolean isUuid(String organizationId) {
        try {
            UUID.fromString(organizationId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
