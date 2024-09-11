package io.codefresh.gradleexample.domain.service;

import io.codefresh.gradleexample.application.dtos.TenderCreateRequest;
import io.codefresh.gradleexample.application.repositories.TenderRepository;
import io.codefresh.gradleexample.domain.model.TenderReq;
import io.codefresh.gradleexample.infrastructure.entity.Tender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenderService {
    private final TenderRepository tenderRepository;

    public List<Tender> getTenders(TenderReq tenderReq) {
        Sort sort = Sort.by(Sort.Direction.fromString(tenderReq.sortDirection()), tenderReq.sortField());
        PageRequest pageRequest =
                PageRequest.of(tenderReq.offset()/tenderReq.limit(), tenderReq.limit(), sort);
        return tenderReq.serviceType().isEmpty() ? tenderRepository.findAll(pageRequest).getContent() :
                tenderRepository.findAllByServiceTypes(tenderReq.serviceType(), pageRequest).getContent();
    }

    public Tender createTender(TenderCreateRequest request) {
        return new Tender();
    }
}
