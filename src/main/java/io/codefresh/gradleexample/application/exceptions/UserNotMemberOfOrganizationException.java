package io.codefresh.gradleexample.application.exceptions;

public class UserNotMemberOfOrganizationException extends RuntimeException {
    public UserNotMemberOfOrganizationException(String message) {
        super(message);
    }
}
