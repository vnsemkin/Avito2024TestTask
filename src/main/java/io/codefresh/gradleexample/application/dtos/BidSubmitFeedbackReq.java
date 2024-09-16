package io.codefresh.gradleexample.application.dtos;

import io.codefresh.gradleexample.application.validators.AppValidator;
import org.springframework.lang.NonNull;

public record BidSubmitFeedbackReq(@NonNull String bidId,
                                   @NonNull String bidFeedback,
                                   @NonNull String username) {
    public BidSubmitFeedbackReq {
        if (!AppValidator.isUuid(bidId)) {
            throw new IllegalArgumentException("Invalid bidId");
        }
        if (username.length() > 50) {
            throw new IllegalArgumentException("Invalid username length");
        }
        if (bidFeedback.length() > 1000) {
            throw new IllegalArgumentException("Invalid feedback length");
        }
    }
}
