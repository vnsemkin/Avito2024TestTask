package io.codefresh.gradleexample.domain.service;

import io.codefresh.gradleexample.application.dtos.*;
import io.codefresh.gradleexample.application.exceptions.*;
import io.codefresh.gradleexample.application.mappers.BidMapper;
import io.codefresh.gradleexample.application.repositories.*;
import io.codefresh.gradleexample.application.validators.AppValidator;
import io.codefresh.gradleexample.domain.util.Checker;
import io.codefresh.gradleexample.infrastructure.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BidService extends AppService {
    private final Checker checker;
    private final BidRepository bidRepository;
    private final BidDecisionRepository bidDecisionRepository;
    private final BidFeedbackRepository bidFeedbackRepository;
    private final TenderRepository tenderRepository;
    private final static String AUTHOR_ID_NOT_MATCH = "AuthorId not match with authorType";
    private final static String BID_NOT_FOUND = "Bid with id %s not found";
    private final static String BID_WITH_VERSION_NOT_FOUND = "Bid with id %s and version %s not found";
    private final static String USER_NOT_MEMBER_OF_ORGANIZATION = "User not member of organization";
    private final static String USER_ALREADY_SUBMITTED_DECISION = "User already submitted decision";
    private final static String USER_ALREADY_LEFT_FEEDBACK = "User already left feedback";

    public BidService(Checker checker,
                      BidRepository bidRepository,
                      EmployeeRepository employeeRepository,
                      BidDecisionRepository bidDecisionRepository,
                      BidFeedbackRepository bidFeedbackRepository,
                      TenderRepository tenderRepository) {
        super(employeeRepository);
        this.checker = checker;
        this.bidRepository = bidRepository;
        this.bidDecisionRepository = bidDecisionRepository;
        this.bidFeedbackRepository = bidFeedbackRepository;
        this.tenderRepository = tenderRepository;
    }

    @Transactional
    public Bid createBid(@NonNull BidCreateReq request) {
        Tender tender = checker.checkEmployeeRights(request.name(), request.tenderId());
        if (!checker.isBidAuthorIdBelongToAuthorType(request.authorType(), request.authorId(), tender)) {
            throw new AuthorIdNotMatchWithAuthorTypeException(AUTHOR_ID_NOT_MATCH);
        }

        Bid bid = BidMapper.createBid(request);
        return bidRepository.save(bid);
    }

    @Transactional
    public List<Bid> getBidsByUsername(@NonNull ReqByUserName tenderReq) {
        PageRequestByUsername pageRequest = getPageRequestIfUserExist(tenderReq);
        return bidRepository.findAllByAuthorId(pageRequest.employee().getId(), pageRequest.page()).getContent();
    }

    @Transactional
    public List<Bid> getBidsByTenderId(@NonNull BidsByTenderIdReq bidsByTenderIdReq) {
        UUID tenderId = UUID.fromString(bidsByTenderIdReq.tenderId());
        PageRequestByUsername pageRequest = getPageRequestIfUserExist(new ReqByUserName(bidsByTenderIdReq.limit(),
                bidsByTenderIdReq.offset(),
                bidsByTenderIdReq.username(),
                bidsByTenderIdReq.sortField(),
                bidsByTenderIdReq.sortDirection()
        ));

        Page<Bid> bids =
                bidRepository.findAllByTenderId(tenderId, pageRequest.page());

        Employee employee = pageRequest.employee();
        UUID organizationId = tenderRepository.findByTenderId(tenderId).orElseThrow(() ->
                new TenderNotFoundException(String.format("Tender %s not found", tenderId))).getOrganizationId();

        Predicate<Bid> author = bid -> bid.getAuthorId().equals(pageRequest.employee().getId());
        Predicate<Bid> memberOfOrganization = bid ->
                employee.getOrganizations().stream().anyMatch(org -> org.getId().equals(organizationId));

        return bids.stream()
                .filter(author.or(memberOfOrganization))
                .collect(Collectors.toList());
    }

    @Transactional
    public Bid changeBidStatus(@NonNull BidChangeStatusReq bidChangeStatusReq) {
        UUID bidId = UUID.fromString(bidChangeStatusReq.bidId());
        Employee employeeIfExist = checker.getEmployeeIfExist(bidChangeStatusReq.username());
        AppValidator appValidator = new AppValidator(tenderRepository);
        Bid bid = bidRepository.findByBidId(bidId).orElseThrow(() ->
                new BidNotFoundException(String.format(BID_NOT_FOUND, bidId)));
        if (appValidator.isUserAuthorOrMemberOfOrganization(employeeIfExist, bid)) {
            bid.setStatus(bidChangeStatusReq.status());
            bid.setUpdatedAt(LocalDateTime.now());
            bid.setVersion(bid.getVersion() + 1);
            return bidRepository.save(bid);
        }
        throw new UserNotMemberOfOrganizationException(USER_NOT_MEMBER_OF_ORGANIZATION);
    }

    public BidStatusResp getBidStatus(@NonNull BidStatusReq bidStatusReq) {
        Bid bid = checkUserExistAndMemberOfOrganization(bidStatusReq.username(), bidStatusReq.bidId());
        return new BidStatusResp(bid.getStatus());
    }

    @Transactional
    public Bid editBid(@NonNull BidFullEditReq bidEditFullReq) {
        Bid bid = checkUserExistAndMemberOfOrganization(bidEditFullReq.username(), bidEditFullReq.bidId());
        if (bidEditFullReq.name() != null) {
            bid.setName(bidEditFullReq.name());
        }
        if (bidEditFullReq.description() != null) {
            bid.setDescription(bidEditFullReq.description());
        }
        return bidRepository.save(BidMapper.cloneBid(bid));
    }

    private Bid checkUserExistAndMemberOfOrganization(@NonNull String username, @NonNull String bidId) {
        Employee employeeIfExist = checker.getEmployeeIfExist(username);
        Bid bid = bidRepository.findByBidId(UUID.fromString(bidId)).orElseThrow(() -> new BidNotFoundException(
                String.format(BID_NOT_FOUND, bidId)));
        Tender tender = tenderRepository.findByTenderId(bid.getTenderId()).orElseThrow(() -> new TenderNotFoundException(
                String.format("Tender %s not found", bid.getTenderId())));
        checker.isUserMemberOfOrganization(tender, employeeIfExist);
        return bid;
    }

    @Transactional
    public Bid submitDecision(@NonNull BidSubmitDecisionReq bidSubmitDecisionReq) {
        Bid bid = checkUserExistAndMemberOfOrganization(bidSubmitDecisionReq.username(), bidSubmitDecisionReq.bidId());
        Employee employee = checker.getEmployeeIfExist(bidSubmitDecisionReq.username());
        BidDecision bidDecision = new BidDecision();
        bidDecision.setBidId(bid.getBidId());
        bidDecision.setAuthorId(employee.getId());
        bidDecision.setDecision(bidSubmitDecisionReq.decision());
        Optional<BidDecision> byAuthorId = bidDecisionRepository
                .findByAuthorId(employee.getId());
        if (byAuthorId.isPresent()) {
            throw new UserAlreadySubmittedDecisionException(USER_ALREADY_SUBMITTED_DECISION);
        }
        bidDecisionRepository.save(bidDecision);
        return bidRepository.findByBidId(bid.getBidId())
                .orElseThrow(() -> new BidNotFoundException("Bid not found"));
    }

    @Transactional
    public Bid submitFeedback(@NonNull BidSubmitFeedbackReq bidSubmitFeedbackReq) {
        Bid bid = checkUserExistAndMemberOfOrganization(bidSubmitFeedbackReq.username(),
                bidSubmitFeedbackReq.bidId());
        Employee employee = checker.getEmployeeIfExist(bidSubmitFeedbackReq.username());
        BidFeedback bidFeedback = new BidFeedback();
        bidFeedback.setBidId(bid.getBidId());
        bidFeedback.setAuthorId(employee.getId());
        bidFeedback.setFeedback(bidSubmitFeedbackReq.bidFeedback());

        Optional<BidFeedback> byAuthorId = bidFeedbackRepository.findByAuthorId(employee.getId());
        if (byAuthorId.isPresent()) {
            throw new UserAlreadyLeftFeedbackException(USER_ALREADY_LEFT_FEEDBACK);
        }

        bidFeedbackRepository.save(bidFeedback);
        return bidRepository.findByBidId(bid.getBidId())
                .orElseThrow(() -> new BidNotFoundException("Bid not found"));
    }

    @Transactional
    public Bid rollbackBid(@NonNull BidRollbackReq bidRollbackReq) {
        checkUserExistAndMemberOfOrganization(bidRollbackReq.username(),
                bidRollbackReq.bidId());
        Bid bid = bidRepository.findBidByVersion(UUID.fromString(bidRollbackReq.bidId()), bidRollbackReq.version())
                .orElseThrow(() -> new BidNotFoundException(String.format(BID_WITH_VERSION_NOT_FOUND,
                        bidRollbackReq.bidId(), bidRollbackReq.version())));
        int lastTenderVersion = bidRepository.getLastTenderVersion(UUID.fromString(bidRollbackReq.bidId()));
        bid.setVersion(lastTenderVersion + 1);
        return bidRepository.save(bid);
    }
}
