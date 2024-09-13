package io.codefresh.gradleexample.application.dtos;

import io.codefresh.gradleexample.application.validators.AppValidator;
import org.springframework.lang.NonNull;

public record BidFullEditReq(@NonNull String bidId,
                             @NonNull String username,
                             String name,
                             String description) {
    public BidFullEditReq {
        if (!AppValidator.isUuid(bidId)) {
            throw new IllegalArgumentException("Invalid bidId");
        }
        if (username.length() > 50) {
            throw new IllegalArgumentException("Invalid username length");
        }
        if (name.length() >= 100) {
            throw new IllegalArgumentException("Invalid name length");
        }
        if (description.length() >= 500) {
            throw new IllegalArgumentException("Invalid description length");
        }
    }
}
