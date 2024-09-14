package io.codefresh.gradleexample.controller;

import io.codefresh.gradleexample.application.dtos.TenderDto;
import io.codefresh.gradleexample.domain.service.BidService;
import io.codefresh.gradleexample.domain.service.TenderService;
import io.codefresh.gradleexample.infrastructure.entity.Tender;
import io.codefresh.gradleexample.presentation.controller.AppController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
@WebMvcTest(AppController.class)
public class AppControllerTest {
    private static final String DEFAULT_LIMIT = "5";
    private static final String DEFAULT_OFFSET = "0";
    private static final String WRONG_SERVICE_TYPE = "Некорректные значения service_type";

    @MockBean
    TenderService tenderService;
    @MockBean
    BidService bidService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void checkServer_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/ping")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    public void getTenders_ReturnsTenderList() throws Exception {
        Tender tender = getTender();

        when(tenderService.getTenders(any())).thenReturn(List.of(tender));

        mockMvc.perform(get("/api/tenders")
                        .param("limit", "5")
                        .param("offset", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tenderId")
                        .value("13bcad3e-5ee9-419b-a101-8c88ed7f33d4"))
                .andExpect(jsonPath("$[0].tenderName").value("Mock Tender Name"))
                .andExpect(jsonPath("$[0].tenderDescription").value("Mock Tender Description"))
                .andExpect(jsonPath("$[0].tenderServiceType").value("TypeA"))
                .andExpect(jsonPath("$[0].tenderStatus").value("Open"))
                .andExpect(jsonPath("$[0].organizationId")
                        .value("02719295-3707-438c-9049-4b959c96fa7b"))
                .andExpect(jsonPath("$[0].tenderVersion").value(1));
    }

    @Test
    public void getTenders_ReturnsBadRequestForInvalidServiceType() throws Exception {
        // Perform GET request with invalid service_type
        mockMvc.perform(get("/api/tenders")
                        .param("service_type", "INVALID_TYPE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(WRONG_SERVICE_TYPE));
    }

    @Test
    public void getTenders_ReturnsNoContentForEmptyList() throws Exception {
        when(tenderService.getTenders(any())).thenReturn(null);
        mockMvc.perform(get("/api/tenders")
                        .param("limit", DEFAULT_LIMIT)
                        .param("offset", DEFAULT_OFFSET)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private TenderDto getTenderDto() {
        return new TenderDto(
                "123",
                "Mock Tender",
                "Mock Description",
                "TypeA",
                "Open",
                "Org123",
                1,
                LocalDateTime.now()
        );
    }

    private Tender getTender() {
        Tender tender = new Tender();
        tender.setTenderId(UUID.fromString("13bcad3e-5ee9-419b-a101-8c88ed7f33d4"));
        tender.setName("Mock Tender Name");
        tender.setDescription("Mock Tender Description");
        tender.setStatus("Open");
        tender.setEmployeeId(UUID.fromString("02719295-3707-438c-9049-4b959c96fa7b"));
        tender.setOrganizationId(UUID.fromString("02719295-3707-438c-9049-4b959c96fa7b"));
        tender.setServiceType("TypeA");
        tender.setVersion(1);
        tender.setCreatedAt(LocalDateTime.now().minusDays(1));
        tender.setUpdatedAt(LocalDateTime.now());

        return tender;
    }
}
