package io.codefresh.gradleexample.domain.model;

import java.util.List;

public record TenderReq(int limit, int offset, List<String> serviceType) {
}
