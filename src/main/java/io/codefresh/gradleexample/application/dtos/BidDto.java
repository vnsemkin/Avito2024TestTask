package io.codefresh.gradleexample.application.dtos;

import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public record BidDto(
       @NonNull String bidId,
       @NonNull String bidName,
       @NonNull String bidDescription,
       @NonNull String bidStatus,
       @NonNull String tenderId,
       @NonNull String bidAuthorType,
       @NonNull String bidAuthorId,
        int bidVersion,
       @NonNull LocalDateTime createdAt
) {}
