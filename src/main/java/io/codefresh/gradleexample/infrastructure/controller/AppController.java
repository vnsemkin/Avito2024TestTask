package io.codefresh.gradleexample.infrastructure.controller;

import io.codefresh.gradleexample.domain.model.TenderReq;
import io.codefresh.gradleexample.application.config.ServiceType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppController {
    public static final String DEFAULT_LIMIT = "5";
    public static final String DEFAULT_OFFSET = "0";

    @GetMapping("/ping")
    public ResponseEntity<String> checkServer() {
        try {
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server not ready");
        }
    }

    @GetMapping
    public ResponseEntity<?> getTenders(
            @RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
            @RequestParam(defaultValue = DEFAULT_OFFSET) int offset,
            @RequestParam(required = false) List<String> service_type) {
        limit = limit > 50 ? limit : 50;

        List<String> serviceType = service_type.stream()
                .filter(ServiceType::contains).collect(Collectors.toList());
        new TenderReq(limit, offset, serviceType);

        // TODO: get list of tenders
        return ResponseEntity.ok("response");
    }


}

