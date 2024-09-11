package io.codefresh.gradleexample.presentation.controller;

import io.codefresh.gradleexample.application.config.TenderServiceType;
import io.codefresh.gradleexample.application.dtos.*;
import io.codefresh.gradleexample.application.mappers.TenderMapper;
import io.codefresh.gradleexample.domain.service.TenderService;
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
    public ResponseEntity<?> createTender(@RequestBody TenderCreateRequest request) {
        return ResponseEntity.ok(TenderMapper.toTenderCreateResponse(tenderService.createTender(request)));
    }

    @GetMapping("/tenders/my")
    public ResponseEntity<?> getMyTenders(
            @RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
            @RequestParam(defaultValue = DEFAULT_OFFSET) int offset,
            @RequestParam String username) {

        limit = Math.min(limit, 50);

        TenderReqByUserName tenderReqByUserName =
                new TenderReqByUserName(limit, offset, username, SORT_FIELD, SORT_DIRECTION);
        List<Tender> tenders = tenderService.getTendersByUsername(tenderReqByUserName);

        return ResponseEntity.ok(TenderMapper.toTenderDtoList(tenders));
    }

    @GetMapping("/tenders/{tenderId}/status")
    public ResponseEntity<?> getTenderStatus(
            @PathVariable("tenderId") String tenderId,
            @RequestParam String username) {

        TenderStatusReq tenderStatusReq = new TenderStatusReq(tenderId, username);
        return ResponseEntity.ok().body(tenderService.getTenderStatus(tenderStatusReq));
    }

    @PutMapping("/tenders/{tenderId}/status")
    public ResponseEntity<?> changeTenderStatus(
            @PathVariable("tenderId") String tenderId,
            @RequestParam String status,
            @RequestParam String username) {

        TenderChangeStatusReq tenderChangeStatusReq = new TenderChangeStatusReq(tenderId, status, username);
        Tender tender = tenderService.changeTenderStatus(tenderChangeStatusReq);
        return ResponseEntity.ok().body(TenderMapper.toTenderChangeStatusResp(tender));
    }

    @PatchMapping("/tenders/{tenderId}/edit")
    public ResponseEntity<?> editTender(@PathVariable("tenderId") String tenderId,
                                        @RequestParam String username,
                                        @RequestBody TenderEditReq request) {
        TenderEditFullReq tenderEditFullReq = new TenderEditFullReq(tenderId, username, request);

        return ResponseEntity.ok().body(TenderMapper.toTenderDto(tenderService.editTender(tenderEditFullReq)));
    }
}

