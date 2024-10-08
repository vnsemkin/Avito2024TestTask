package io.codefresh.gradleexample.application.dtos;

import org.springframework.lang.NonNull;

import java.util.List;

public record TenderReq(int limit, int offset,
                        @NonNull List<String> serviceType,
                        @NonNull String sortField,
                        @NonNull String sortDirection) {
}
