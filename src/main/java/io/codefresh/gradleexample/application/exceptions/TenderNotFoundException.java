package io.codefresh.gradleexample.application.exceptions;

public class TenderNotFoundException extends RuntimeException {
    private static final String REASON = "reason: ";
    public TenderNotFoundException(String message) {
        super(REASON + message);
    }
}
