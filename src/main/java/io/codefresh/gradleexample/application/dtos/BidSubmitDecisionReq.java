package io.codefresh.gradleexample.application.dtos;

import io.codefresh.gradleexample.application.config.BidDecisionStatus;
import io.codefresh.gradleexample.application.validators.AppValidator;
import lombok.NonNull;

public record BidSubmitDecisionReq(@NonNull String bidId,
                                   @NonNull String decision,
                                   @NonNull String username) {
    public BidSubmitDecisionReq{
        if (!AppValidator.isUuid(bidId)) {
            throw new IllegalArgumentException("Invalid bidId");
        }
        if (!BidDecisionStatus.contains(decision)) {
            throw new IllegalArgumentException("Invalid decision");
        }
        if (username.length() > 50) {
            throw new IllegalArgumentException("Invalid username length");
        }
    }
}
