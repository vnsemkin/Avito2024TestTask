package io.codefresh.gradleexample.application.exceptions;

public class UserAlreadySubmittedDecisionException extends RuntimeException {
    public UserAlreadySubmittedDecisionException(String message) {
        super(message);
    }
}
