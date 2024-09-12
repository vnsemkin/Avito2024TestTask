package io.codefresh.gradleexample.application.dtos;

import org.springframework.lang.NonNull;

public record ReqByUserName(int limit, int offset,
                            @NonNull String username,
                            @NonNull String sortField,
                            @NonNull String sortDirection) {
    private static final String INVALID_USERNAME = "Username must be less than 50 characters.";

    public ReqByUserName {
        if (username.length() > 50) {
            throw new IllegalArgumentException(INVALID_USERNAME);
        }
    }
}
