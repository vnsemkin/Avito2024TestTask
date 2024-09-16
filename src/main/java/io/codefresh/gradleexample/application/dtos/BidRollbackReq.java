package io.codefresh.gradleexample.application.dtos;

import io.codefresh.gradleexample.application.validators.AppValidator;
import org.springframework.lang.NonNull;

public record BidRollbackReq(@NonNull String bidId,
                             int version,
                             @NonNull String username) {
    public BidRollbackReq {
        if (!AppValidator.isUuid(bidId)) {
            throw new IllegalArgumentException("Invalid bidId");
        }
        if (version < 1) {
            throw new IllegalArgumentException("Invalid version");
        }
        if (username.length() > 50) {
            throw new IllegalArgumentException("Invalid username length");
        }
    }
}
