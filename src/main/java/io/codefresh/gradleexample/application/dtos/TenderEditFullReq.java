package io.codefresh.gradleexample.application.dtos;

public record TenderEditFullReq(String tenderId,
                                String username,
                                TenderEditReq request) {

    public TenderEditFullReq {
        if (tenderId.length() > 100) {
            throw new IllegalArgumentException("TenderId must be less than 100 characters.");
        }
        if (username.length() > 50) {
            throw new IllegalArgumentException("Username must be less than 50 characters.");
        }
    }
}