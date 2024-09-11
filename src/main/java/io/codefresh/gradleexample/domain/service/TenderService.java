package io.codefresh.gradleexample.domain.service;

import io.codefresh.gradleexample.application.config.TenderStatus;
import io.codefresh.gradleexample.application.dtos.*;
import io.codefresh.gradleexample.application.exceptions.TenderNotFoundException;
import io.codefresh.gradleexample.application.exceptions.UserNotFoundException;
import io.codefresh.gradleexample.application.exceptions.UserNotMemberOfOrganizationException;
import io.codefresh.gradleexample.application.mappers.TenderMapper;
import io.codefresh.gradleexample.application.repositories.EmployeeRepository;
import io.codefresh.gradleexample.application.repositories.TenderRepository;
import io.codefresh.gradleexample.infrastructure.entity.Employee;
import io.codefresh.gradleexample.infrastructure.entity.Tender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenderService {
    private final static String STATUS_PUBLISHED = "Published";
    private final static String USERNAME_NOT_FOUND = "User with username %s not found";
    private final static String TENDER_NOT_FOUND = "Tender with tenderId %s not found";
    private final static String USER_NOT_MEMBER_OF_ORGANIZATION =
            "User with username %s is not a member of organization %s";
    private final TenderRepository tenderRepository;
    private final EmployeeRepository employeeRepository;

    public List<Tender> getTenders(TenderReq tenderReq) {
        Sort sort = Sort.by(Sort.Direction.fromString(tenderReq.sortDirection()), tenderReq.sortField());
        PageRequest pageRequest =
                PageRequest.of(tenderReq.offset() / tenderReq.limit(), tenderReq.limit(), sort);
        return tenderReq.serviceType().isEmpty() ? tenderRepository.findAllWithStatusPublished(STATUS_PUBLISHED,
                pageRequest).getContent() :
                tenderRepository.findAllByServiceTypes(tenderReq.serviceType(),
                        STATUS_PUBLISHED, pageRequest).getContent();
    }

    public Tender createTender(TenderCreateRequest request) {

        Employee creator = employeeRepository.findByUsername(request.creatorUsername())
                .orElseThrow(() ->
                        new UserNotFoundException(String.format(USERNAME_NOT_FOUND, request.creatorUsername())));

        creator.getOrganizations()
                .stream()
                .filter(org -> org.getId().equals(UUID.fromString(request.organizationId())))
                .findFirst()
                .orElseThrow(() ->
                        new UserNotMemberOfOrganizationException(String.format(USER_NOT_MEMBER_OF_ORGANIZATION,
                                request.creatorUsername(), request.organizationId())));

        Tender newTender = TenderMapper.toTender(request, request.organizationId(), TenderStatus.CREATED.getValue());

        return tenderRepository.save(newTender);
    }

    public List<Tender> getTendersByUsername(TenderReqByUserName tenderReq) {
        Sort sort = Sort.by(Sort.Direction.fromString(tenderReq.sortDirection()), tenderReq.sortField());
        PageRequest pageRequest =
                PageRequest.of(tenderReq.offset() / tenderReq.limit(), tenderReq.limit(), sort);

        Employee employee = employeeRepository.findByUsername(tenderReq.username())
                .orElseThrow(() -> new UserNotFoundException(String.format(USERNAME_NOT_FOUND, tenderReq.username())));

        return tenderRepository.findAllByEmployeeId(employee.getId(), pageRequest).getContent();
    }

    public String getTenderStatus(TenderStatusReq tenderStatusReq) {
        Tender tender = checkEmployeeRights(tenderStatusReq.username(), tenderStatusReq.tenderId());
        return tender.getStatus();
    }

    public Tender changeTenderStatus(TenderChangeStatusReq tenderChangeStatusReq) {
        Tender tender = checkEmployeeRights(tenderChangeStatusReq.username(), tenderChangeStatusReq.tenderId());
        tender.setStatus(tenderChangeStatusReq.status());
        tender.setVersion(tender.getVersion() + 1);
        return tenderRepository.save(tender);
    }

    public Tender editTender(TenderEditFullReq tenderEditFullReq) {
        Tender tender = checkEmployeeRights(tenderEditFullReq.username(), tenderEditFullReq.tenderId());
        if (tenderEditFullReq.request().name() != null) {
            tender.setName(tenderEditFullReq.request().name());
        }
        if (tenderEditFullReq.request().description() != null) {
            tender.setDescription(tenderEditFullReq.request().description());
        }
        if (tenderEditFullReq.request().serviceType() != null) {
            tender.setServiceType(tenderEditFullReq.request().serviceType());
        }
        tender.setVersion(tender.getVersion() + 1);
        return tenderRepository.save(tender);
    }


    private Tender checkEmployeeRights(String username, String tenderId) {
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
}
