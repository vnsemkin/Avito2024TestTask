package io.codefresh.gradleexample.application.dtos;

import io.codefresh.gradleexample.application.config.BidAuthor;
import io.codefresh.gradleexample.application.validators.AppValidator;
import org.springframework.lang.NonNull;

public record BidCreateReq(
       @NonNull String name,
       @NonNull String description,
       @NonNull String tenderId,
       @NonNull String authorType,
       @NonNull String authorId
) {

    private static final String INVALID_BID_NAME =
            "Invalid bidName: should not be null and max length is 100 characters";
    private static final String INVALID_BID_DESCRIPTION =
            "Invalid bidDescription: should not be null and max length is 500 characters";
    private static final String INVALID_TENDER_ID =
            "Invalid tenderId: should not be null and max length is 100 characters";
    private static final String INVALID_AUTHOR_TYPE =
            "Invalid bidAuthorType: should be one of 'Organization', 'User'";
    private static final String INVALID_AUTHOR_ID =
            "Invalid authorId: should not be null and must be a valid UUID";

    public BidCreateReq {
        if (name.length() > 100) {
            throw new IllegalArgumentException(INVALID_BID_NAME);
        }
        if (description.length() > 500) {
            throw new IllegalArgumentException(INVALID_BID_DESCRIPTION);
        }
        if (tenderId.length() > 100) {
            throw new IllegalArgumentException(INVALID_TENDER_ID);
        }
        if (!BidAuthor.contains(authorType)) {
            throw new IllegalArgumentException(INVALID_AUTHOR_TYPE);
        }
        if (!AppValidator.isUuid(authorId)) {
            throw new IllegalArgumentException(INVALID_AUTHOR_ID);
        }
    }
}

