package io.codefresh.gradleexample.domain.service;

import io.codefresh.gradleexample.application.config.PageRequestByUsername;
import io.codefresh.gradleexample.application.config.TenderBidStatus;
import io.codefresh.gradleexample.application.dtos.BidCreateReq;
import io.codefresh.gradleexample.application.dtos.ReqByUserName;
import io.codefresh.gradleexample.application.exceptions.AuthorIdNotMatchWithAuthorTypeException;
import io.codefresh.gradleexample.application.repositories.BidRepository;
import io.codefresh.gradleexample.application.repositories.EmployeeRepository;
import io.codefresh.gradleexample.domain.util.Checker;
import io.codefresh.gradleexample.infrastructure.entity.Bid;
import io.codefresh.gradleexample.infrastructure.entity.Tender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BidService extends AppService{
    private final Checker checker;
    private final BidRepository bidRepository;
    private final static String AUTHOR_ID_NOT_MATCH = "AuthorId not match with authorType";

    public BidService(Checker checker, BidRepository bidRepository, EmployeeRepository employeeRepository) {
        super(employeeRepository);
        this.checker = checker;
        this.bidRepository = bidRepository;
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
        PageRequestByUsername pageRequest = getPageRequest(tenderReq);
        return bidRepository.findAllByAuthorId(pageRequest.employee().getId(), pageRequest.page()).getContent();
    }
}
