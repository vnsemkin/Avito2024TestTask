package io.codefresh.gradleexample.domain.util;

import io.codefresh.gradleexample.application.exceptions.TenderNotFoundException;
import io.codefresh.gradleexample.application.exceptions.UserNotFoundException;
import io.codefresh.gradleexample.application.exceptions.UserNotMemberOfOrganizationException;
import io.codefresh.gradleexample.application.repositories.EmployeeRepository;
import io.codefresh.gradleexample.application.repositories.TenderRepository;
import io.codefresh.gradleexample.infrastructure.entity.Employee;
import io.codefresh.gradleexample.infrastructure.entity.Tender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Checker {
    private final static String USERNAME_NOT_FOUND = "User with username %s not found";
    private final static String TENDER_NOT_FOUND = "Tender with tenderId %s not found";
    private final static String USER_NOT_MEMBER_OF_ORGANIZATION =
            "User with username %s is not a member of organization %s";
    private final TenderRepository tenderRepository;
    private final EmployeeRepository employeeRepository;

    public Tender checkEmployeeRights(String username, String tenderId) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format(USERNAME_NOT_FOUND, username)));
        Tender tender = tenderRepository.findById(UUID.fromString(tenderId))
                .orElseThrow(() ->
                        new TenderNotFoundException(String.format(TENDER_NOT_FOUND, tenderId)));
        if (employee.getOrganizations().stream().noneMatch(org -> org.getId().equals(tender.getOrganizationId()))) {
            throw new UserNotMemberOfOrganizationException(String.format(USER_NOT_MEMBER_OF_ORGANIZATION,
                    username, tender.getOrganizationId()));
        }
        return tender;
    }

    public boolean isBidAuthorIdBelongToAuthorType(String authorType, String authorId, Tender tender) {
        return authorType.equals("Organization") ? tender.getOrganizationId().equals(UUID.fromString(authorId)) :
                tender.getEmployeeId().equals(UUID.fromString(authorId));
    }

    public Employee getEmployeeIfExist(String username) {
        return employeeRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format(USERNAME_NOT_FOUND, username)));
    }
}
