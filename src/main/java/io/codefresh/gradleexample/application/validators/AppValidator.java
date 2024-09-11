package io.codefresh.gradleexample.application.validators;

import java.util.UUID;

public class AppValidator {
    public static boolean isUuid(String organizationId) {
        try {
            UUID.fromString(organizationId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
