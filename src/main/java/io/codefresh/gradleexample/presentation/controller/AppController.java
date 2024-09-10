package io.codefresh.gradleexample.presentation.controller;

import io.codefresh.gradleexample.application.config.TenderServiceType;
import io.codefresh.gradleexample.domain.model.TenderReq;
import io.codefresh.gradleexample.domain.service.TenderService;
import io.codefresh.gradleexample.infrastructure.entity.Tender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private static final int LIMIT_FIFTY = 50;
    private final TenderService tenderService;

    @GetMapping("/ping")
    public ResponseEntity<String> checkServer() {
        try {
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server not ready");
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
        return tenders == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(tenders);
    }
}

