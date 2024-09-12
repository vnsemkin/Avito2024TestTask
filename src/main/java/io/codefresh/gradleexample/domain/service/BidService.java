package io.codefresh.gradleexample.domain.service;

import io.codefresh.gradleexample.application.config.TenderBidStatus;
import io.codefresh.gradleexample.application.dtos.BidCreateReq;
import io.codefresh.gradleexample.application.dtos.BidsByTenderIdReq;
import io.codefresh.gradleexample.application.dtos.PageRequestByUsername;
import io.codefresh.gradleexample.application.dtos.ReqByUserName;
import io.codefresh.gradleexample.application.exceptions.AuthorIdNotMatchWithAuthorTypeException;
import io.codefresh.gradleexample.application.exceptions.TenderNotFoundException;
import io.codefresh.gradleexample.application.repositories.BidRepository;
import io.codefresh.gradleexample.application.repositories.EmployeeRepository;
import io.codefresh.gradleexample.application.repositories.TenderRepository;
import io.codefresh.gradleexample.domain.util.Checker;
import io.codefresh.gradleexample.infrastructure.entity.Bid;
import io.codefresh.gradleexample.infrastructure.entity.Employee;
import io.codefresh.gradleexample.infrastructure.entity.Tender;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BidService extends AppService {
    private final Checker checker;
    private final BidRepository bidRepository;
    private final TenderRepository tenderRepository;
    private final static String AUTHOR_ID_NOT_MATCH = "AuthorId not match with authorType";

    public BidService(Checker checker, BidRepository bidRepository,
                      EmployeeRepository employeeRepository, TenderRepository tenderRepository) {
        super(employeeRepository);
        this.checker = checker;
        this.bidRepository = bidRepository;
        this.tenderRepository = tenderRepository;
    }

    public Bid createBid(BidCreateReq request) {
        Tender tender = checker.checkEmployeeRights(request.name(), request.tenderId());
        if (!checker.isBidAuthorIdBelongToAuthorType(request.authorType(), request.authorId(), tender)) {
            throw new AuthorIdNotMatchWithAuthorTypeException(AUTHOR_ID_NOT_MATCH);
        }

        Bid bid = new Bid();
        bid.setName(request.name());
        bid.setDescription(request.description());
        bid.setStatus(TenderBidStatus.CREATED.getValue());
        bid.setTenderId(UUID.fromString(request.tenderId()));
        bid.setAuthorType(request.authorType());
        bid.setAuthorId(UUID.fromString(request.authorId()));
        bid.setVersion(1);
        bid.setCreatedAt(LocalDateTime.now());
        bid.setUpdatedAt(LocalDateTime.now());
        return bidRepository.save(bid);
    }

    public List<Bid> getBidsByUsername(ReqByUserName tenderReq) {
        PageRequestByUsername pageRequest = getPageRequestIfUserExist(tenderReq);
        return bidRepository.findAllByAuthorId(pageRequest.employee().getId(), pageRequest.page()).getContent();
    }


    public List<Bid> getBidsByTenderId(BidsByTenderIdReq bidsByTenderIdReq) {
        UUID uuid = UUID.fromString(bidsByTenderIdReq.tenderId());
        PageRequestByUsername pageRequest = getPageRequestIfUserExist(new ReqByUserName(bidsByTenderIdReq.limit(),
                bidsByTenderIdReq.offset(),
                bidsByTenderIdReq.username(),
                bidsByTenderIdReq.sortField(),
                bidsByTenderIdReq.sortDirection()
        ));

        Page<Bid> bids =
                bidRepository.findAllByTenderId(uuid, pageRequest.page());

        Employee employee = pageRequest.employee();
        UUID organizationId = tenderRepository.findById(uuid).orElseThrow(() ->
                new TenderNotFoundException(String.format("Tender %s not found", uuid))).getOrganizationId();

        Predicate<Bid> author = bid -> bid.getAuthorId().equals(pageRequest.employee().getId());
        Predicate<Bid> memberOfOrganization = bid ->
                employee.getOrganizations().stream().anyMatch(org -> org.getId().equals(organizationId));

        return bids.stream()
                .filter(author.or(memberOfOrganization))
                .collect(Collectors.toList());
    }
}
