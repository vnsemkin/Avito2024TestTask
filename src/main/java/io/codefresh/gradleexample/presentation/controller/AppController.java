package io.codefresh.gradleexample.presentation.controller;

import io.codefresh.gradleexample.application.config.TenderServiceType;
import io.codefresh.gradleexample.application.dtos.*;
import io.codefresh.gradleexample.application.mappers.BidMapper;
import io.codefresh.gradleexample.application.mappers.TenderMapper;
import io.codefresh.gradleexample.domain.service.BidService;
import io.codefresh.gradleexample.domain.service.TenderService;
import io.codefresh.gradleexample.infrastructure.entity.Bid;
import io.codefresh.gradleexample.infrastructure.entity.Tender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppController {
    private static final String DEFAULT_LIMIT = "5";
    private static final String DEFAULT_OFFSET = "0";
    private static final String SORT_FIELD = "name";
    private static final String SORT_DIRECTION = "ASC";
    private static final String WRONG_SERVICE_TYPE = "Некорректные значения service_type";
    private static final String SERVER_NOT_READY = "Server not ready";
    private static final int LIMIT_FIFTY = 50;
    private final TenderService tenderService;
    private final BidService bidService;

    @GetMapping("/ping")
    public ResponseEntity<String> checkServer() {
        try {
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(SERVER_NOT_READY);
        }
    }

    @GetMapping("/tenders")
    public ResponseEntity<?> getTenders(
            @RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
            @RequestParam(defaultValue = DEFAULT_OFFSET) int offset,
            @RequestParam(required = false) List<String> service_type) {

        if (service_type != null && service_type.stream().anyMatch(s -> !TenderServiceType.contains(s))) {
            return ResponseEntity.badRequest().body(WRONG_SERVICE_TYPE);
        }
        limit = Math.min(limit, LIMIT_FIFTY);
        List<String> serviceType = service_type == null ? List.of() : service_type;
        TenderReq tenderReq = new TenderReq(limit, offset, serviceType, SORT_FIELD, SORT_DIRECTION);
        List<Tender> tenders = tenderService.getTenders(tenderReq);
        return tenders == null ? ResponseEntity.noContent().build() :
                ResponseEntity.ok(TenderMapper.toTenderDtoList(tenders));
    }

    @PostMapping("/tenders/new")
    public ResponseEntity<TenderCreateResponse> createTender(@RequestBody TenderCreateRequest request) {
        return ResponseEntity.ok(TenderMapper.toTenderCreateResponse(tenderService.createTender(request)));
    }

    @GetMapping("/tenders/my")
    public ResponseEntity<List<TenderDto>> getMyTenders(
            @RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
            @RequestParam(defaultValue = DEFAULT_OFFSET) int offset,
            @RequestParam String username) {
        limit = Math.min(limit, 50);
        ReqByUserName reqByUserName =
                new ReqByUserName(limit, offset, username, SORT_FIELD, SORT_DIRECTION);
        List<Tender> tenders = tenderService.getTendersByUsername(reqByUserName);
        return ResponseEntity.ok(TenderMapper.toTenderDtoList(tenders));
    }

    @GetMapping("/tenders/{tenderId}/status")
    public ResponseEntity<String> getTenderStatus(
            @PathVariable("tenderId") String tenderId,
            @RequestParam String username) {
        TenderStatusReq tenderStatusReq = new TenderStatusReq(tenderId, username);
        return ResponseEntity.ok().body(tenderService.getTenderStatus(tenderStatusReq));
    }

    @PutMapping("/tenders/{tenderId}/status")
    public ResponseEntity<TenderChangeStatusResp> changeTenderStatus(
            @PathVariable("tenderId") String tenderId,
            @RequestParam String status,
            @RequestParam String username) {
        TenderChangeStatusReq tenderChangeStatusReq = new TenderChangeStatusReq(tenderId, status, username);
        Tender tender = tenderService.changeTenderStatus(tenderChangeStatusReq);
        return ResponseEntity.ok().body(TenderMapper.toTenderChangeStatusResp(tender));
    }

    @PatchMapping("/tenders/{tenderId}/edit")
    public ResponseEntity<TenderDto> editTender(@PathVariable("tenderId") String tenderId,
                                                @RequestParam String username,
                                                @RequestBody TenderEditReq request) {
        TenderEditFullReq tenderEditFullReq = new TenderEditFullReq(tenderId, username, request);
        return ResponseEntity.ok().body(TenderMapper.toTenderDto(tenderService.editTender(tenderEditFullReq)));
    }

    @PutMapping("/tenders/{tenderId}/rollback/{version}")
    public ResponseEntity<TenderDto> rollbackTender(@PathVariable("tenderId") String tenderId,
                                                    @PathVariable("version") int version,
                                                    @RequestParam String username) {
        TenderRollbackReq tenderRollbackReq = new TenderRollbackReq(tenderId, version, username);
        return ResponseEntity.ok().body(TenderMapper.toTenderDto(tenderService.rollbackTender(tenderRollbackReq)));
    }


    @PostMapping("/bids/new")
    public ResponseEntity<BidDto> createBid(@RequestBody BidCreateReq bidCreateReq) {
        return ResponseEntity.ok(BidMapper.toBidDto(bidService.createBid(bidCreateReq)));
    }

    @GetMapping("/bids/my")
    public ResponseEntity<List<BidDto>> getMyBids(
            @RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
            @RequestParam(defaultValue = DEFAULT_OFFSET) int offset,
            @RequestParam String username) {
        limit = Math.min(limit, 50);
        ReqByUserName bidRequestByUserName =
                new ReqByUserName(limit, offset, username, SORT_FIELD, SORT_DIRECTION);
        List<Bid> bids = bidService.getBidsByUsername(bidRequestByUserName);
        return ResponseEntity.ok(BidMapper.toBidDtoList(bids));
    }

    @GetMapping("/bids/{tenderId}/list")
    public ResponseEntity<List<BidDto>> getBidsByTenderId(
            @PathVariable("tenderId") String tenderId,
            @RequestParam String username,
            @RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
            @RequestParam(defaultValue = DEFAULT_OFFSET) int offset) {
        limit = Math.min(limit, 50);
        BidsByTenderIdReq bidsByTenderIdReq = new BidsByTenderIdReq(limit,
                offset, tenderId, username, SORT_FIELD, SORT_DIRECTION);

        List<Bid> bids = bidService.getBidsByTenderId(bidsByTenderIdReq);
        return ResponseEntity.ok(BidMapper.toBidDtoList(bids));
    }

    @GetMapping("/bids/{bidId}/status")
    public ResponseEntity<BidStatusResp> getBidStatus(@PathVariable("bidId") String bidId,
                                                      @RequestParam String username) {
        BidStatusReq bidStatusReq = new BidStatusReq(bidId, username);
        return ResponseEntity.ok().body(bidService.getBidStatus(bidStatusReq));
    }

    @PutMapping("/bids/{bidId}/status")
    public ResponseEntity<BidDto> changeBidStatus(@PathVariable("bidId") String bidId,
                                                  @RequestParam("status") String status,
                                                  @RequestParam("username") String username) {
        BidChangeStatusReq bidChangeStatusReq = new BidChangeStatusReq(bidId, status, username);
        Bid bid = bidService.changeBidStatus(bidChangeStatusReq);
        return ResponseEntity.ok(BidMapper.toBidDto(bid));
    }

    @PatchMapping("/bids/{bidId}/edit")
    public ResponseEntity<BidDto> editBid(@PathVariable("bidId") String bidId,
                                          @RequestParam String username,
                                          @RequestBody BidEditReq request) {
        BidFullEditReq bidEditFullReq = new BidFullEditReq(bidId, username, request.name(), request.description());
        return ResponseEntity.ok(BidMapper.toBidDto(bidService.editBid(bidEditFullReq)));
    }
}

