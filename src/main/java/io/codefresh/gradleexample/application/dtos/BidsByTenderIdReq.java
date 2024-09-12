package io.codefresh.gradleexample.application.dtos;

import io.codefresh.gradleexample.application.validators.AppValidator;
import org.springframework.lang.NonNull;

public record BidsByTenderIdReq(int limit, int offset,
                                @NonNull String tenderId,
                                @NonNull String username,
                                @NonNull String sortField,
                                @NonNull String sortDirection) {
    private static final String INVALID_USERNAME = "Username must be less than 50 characters.";
    private static final String INVALID_TENDER_ID = "Invalid tenderId";

    public BidsByTenderIdReq {
        if (username.length() > 50) {
            throw new IllegalArgumentException(INVALID_USERNAME);
        }
        if(!AppValidator.isUuid(tenderId)) {
            throw new IllegalArgumentException(INVALID_TENDER_ID);
        }
    }
}