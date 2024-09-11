package io.codefresh.gradleexample.application.exceptions;

public class TenderNotFoundException extends RuntimeException {
    public TenderNotFoundException(String message) {
        super(message);
    }
}
