package io.codefresh.gradleexample.application.dtos;

import io.codefresh.gradleexample.application.validators.AppValidator;
import org.springframework.lang.NonNull;

public record BidStatusReq(@NonNull String bidId,
                           @NonNull String username) {
    private static final String INVALID_USERNAME_LENGTH = "Invalid username length";
    private static final String INVALID_BID_ID = "Invalid bidId";

    public BidStatusReq {
        if (!AppValidator.isUuid(bidId)) {
            throw new IllegalArgumentException(INVALID_BID_ID);
        }
        if (username.length() > 50) {
            throw new IllegalArgumentException(INVALID_USERNAME_LENGTH);
        }
    }
}
