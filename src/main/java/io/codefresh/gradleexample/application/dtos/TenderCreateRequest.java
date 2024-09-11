package io.codefresh.gradleexample.application.dtos;

import io.codefresh.gradleexample.application.config.TenderServiceType;
import io.codefresh.gradleexample.application.validators.AppValidator;
import org.springframework.lang.NonNull;


public record TenderCreateRequest(
        @NonNull String tenderName,
        @NonNull String tenderDescription,
        @NonNull String tenderServiceType,
        @NonNull String organizationId,
        @NonNull String creatorUsername) {
    private static final String INVALID_TENDER_NAME =
            "Invalid tenderName: should not be null and max length is 100 characters";
    private static final String INVALID_TENDER_DESCRIPTION =
            "Invalid tenderDescription: should not be null and max length is 500 characters";
    private static final String INVALID_TENDER_SERVICE_TYPE =
            "Invalid tenderServiceType: should be one of 'Construction', 'Delivery', 'Manufacture'";
    private static final String INVALID_ORGANIZATION_ID =
            "Invalid organizationId: should not be null and max length is 100 characters";
    private static final String INVALID_CREATOR_USERNAME =
            "Invalid creatorUsername: should not be null and max length is 100 characters";

    public TenderCreateRequest {
        if (tenderName.length() > 100) {
            throw new IllegalArgumentException(INVALID_TENDER_NAME);
        }
        if (tenderDescription.length() > 500) {
            throw new IllegalArgumentException(INVALID_TENDER_DESCRIPTION);
        }
        if (!TenderServiceType.contains(tenderServiceType)) {
            throw new IllegalArgumentException(INVALID_TENDER_SERVICE_TYPE);
        }
        if (organizationId.length() > 100 || AppValidator.isUuid(organizationId)) {
            throw new IllegalArgumentException(INVALID_ORGANIZATION_ID);
        }
        if (creatorUsername.length() > 100) {
            throw new IllegalArgumentException(INVALID_CREATOR_USERNAME);
        }
    }
}
