package io.codefresh.gradleexample.application.validators;

import io.codefresh.gradleexample.application.exceptions.TenderNotFoundException;
import io.codefresh.gradleexample.application.repositories.TenderRepository;
import io.codefresh.gradleexample.infrastructure.entity.Bid;
import io.codefresh.gradleexample.infrastructure.entity.Employee;
import org.springframework.lang.NonNull;

import java.util.UUID;
import java.util.function.Predicate;

public class AppValidator {
    private final TenderRepository tenderRepository;

    public AppValidator(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public static boolean isUuid(String organizationId) {
        try {
            UUID.fromString(organizationId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isUserAuthorOrMemberOfOrganization(@NonNull Employee employee, @NonNull Bid bid) {
        UUID tenderId = bid.getTenderId();
        UUID organizationId = tenderRepository.findById(tenderId).orElseThrow(() ->
                new TenderNotFoundException(String.format("Tender %s not found", tenderId))).getOrganizationId();

        Predicate<Bid> author = testBid -> bid.getAuthorId().equals(employee.getId());
        Predicate<Bid> memberOfOrganization = testBid ->
                employee.getOrganizations().stream().anyMatch(org -> org.getId().equals(organizationId));

        return author.or(memberOfOrganization).test(bid);
    }
}

