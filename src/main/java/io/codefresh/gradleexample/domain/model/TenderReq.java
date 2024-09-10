package io.codefresh.gradleexample.domain.model;

import org.springframework.lang.NonNull;

import java.util.List;

public record TenderReq(int limit, int offset,
                        @NonNull List<String> serviceType,
                        @NonNull String sortField,
                        @NonNull String sortDirection) {
}
