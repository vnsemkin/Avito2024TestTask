package io.codefresh.gradleexample.application.config;

import io.codefresh.gradleexample.infrastructure.entity.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public record PageRequestByUsername(@NonNull Pageable page, @NonNull Employee employee) {
}
