package io.codefresh.gradleexample.application.exceptions;

public class UserAlreadyLeftFeedbackException extends RuntimeException {
    public UserAlreadyLeftFeedbackException(String message) {
        super(message);
    }
}
