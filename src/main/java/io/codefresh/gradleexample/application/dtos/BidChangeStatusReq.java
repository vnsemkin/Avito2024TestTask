package io.codefresh.gradleexample.application.dtos;

import io.codefresh.gradleexample.application.config.TenderBidStatus;
import io.codefresh.gradleexample.application.validators.AppValidator;
import org.springframework.lang.NonNull;

public record BidChangeStatusReq(@NonNull String bidId,
                                 @NonNull String status,
                                 @NonNull String username) {
    private static final String INVALID_BID_ID = "Invalid bidId";
    private static final String INVALID_STATUS = "Invalid status";
    private static final String INVALID_USERNAME = "Invalid username";

    public BidChangeStatusReq {
        if (!AppValidator.isUuid(bidId)) {
            throw new IllegalArgumentException(INVALID_BID_ID);
        }
        if (!TenderBidStatus.contains(status)) {
            throw new IllegalArgumentException(INVALID_STATUS);
        }
        if (username.length() > 50) {
            throw new IllegalArgumentException(INVALID_USERNAME);
        }
    }
}