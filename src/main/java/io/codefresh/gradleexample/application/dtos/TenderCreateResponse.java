package io.codefresh.gradleexample.application.dtos;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record TenderCreateResponse(
       @NonNull String tenderId,
       @NonNull String tenderName,
       @NonNull String tenderDescription,
       @NonNull String tenderServiceType,
       @NonNull String tenderStatus,
       @NonNull String organizationId,
        int tenderVersion,
       @NonNull LocalDateTime createdAt
) {}

