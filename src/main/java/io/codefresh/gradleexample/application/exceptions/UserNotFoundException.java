package io.codefresh.gradleexample.application.exceptions;

public class UserNotFoundException extends RuntimeException {
    private static final String REASON = "reason: ";
    public UserNotFoundException(String message) {super(REASON + message);
    }
}
