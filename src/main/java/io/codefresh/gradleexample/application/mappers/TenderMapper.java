package io.codefresh.gradleexample.application.mappers;

import io.codefresh.gradleexample.application.dtos.TenderDto;
import io.codefresh.gradleexample.infrastructure.entity.Tender;

import java.util.List;

public class TenderMapper {

    // Single Tender to TenderDto conversion
    public static TenderDto toTenderDto(Tender tender) {
        return new TenderDto(
                tender.getId().toString(),
                tender.getName(),
                tender.getDescription(),
                tender.getServiceType(),
                tender.getStatus(),
                tender.getOrganizationId(),
                tender.getVersion(),
                tender.getCreatedAt()
        );
    }

    public static List<TenderDto> toTenderDtoList(List<Tender> tenders) {
        return tenders.stream()
                .map(TenderMapper::toTenderDto).toList();
    }
}
