package io.codefresh.gradleexample.application.dtos;

import io.codefresh.gradleexample.application.config.TenderBidStatus;
import io.codefresh.gradleexample.application.validators.AppValidator;
import org.springframework.lang.NonNull;

import java.util.Arrays;

public record TenderChangeStatusReq(
        @NonNull String tenderId,
        @NonNull String status,
        @NonNull String username) {
    private static final String INVALID_USERNAME = "Username must be less than 50 characters.";
    private static final String INVALID_TENDER_ID = "Invalid tenderId: should be UUID";
    private static final String INVALID_STATUS = "Invalid status: should be one of %s";

    public TenderChangeStatusReq {
        if (!AppValidator.isUuid(tenderId)) {
            throw new IllegalArgumentException(INVALID_TENDER_ID);
        }
        if (username.length() > 50) {
            throw new IllegalArgumentException(INVALID_USERNAME);
        }
        if (!TenderBidStatus.contains(status)) {
            throw new IllegalArgumentException(String.format(INVALID_STATUS,
                    Arrays.stream(TenderBidStatus.values())
                            .map(TenderBidStatus::getValue).toList()));
        }
    }
}