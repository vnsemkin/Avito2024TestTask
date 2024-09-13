package io.codefresh.gradleexample.application.dtos;

import io.codefresh.gradleexample.application.validators.AppValidator;
import org.springframework.lang.NonNull;

public record TenderRollbackReq(@NonNull String tenderId,
                                int version,
                                @NonNull String username) {
    private static final String INVALID_TENDER_ID = "Invalid tenderId";
    private static final String INVALID_USERNAME_LENGTH = "Invalid username length";
    private static final String INVALID_VERSION = "Invalid version";

    public TenderRollbackReq {
        if (!AppValidator.isUuid(tenderId)) {
            throw new IllegalArgumentException(INVALID_TENDER_ID);
        }
        if (version < 1) {
            throw new IllegalArgumentException(INVALID_VERSION);
        }
        if (username.length() > 100) {
            throw new IllegalArgumentException(INVALID_USERNAME_LENGTH);
        }
    }
}
