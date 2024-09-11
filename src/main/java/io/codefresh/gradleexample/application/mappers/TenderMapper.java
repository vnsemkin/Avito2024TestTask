package io.codefresh.gradleexample.application.mappers;

import io.codefresh.gradleexample.application.dtos.TenderCreateRequest;
import io.codefresh.gradleexample.application.dtos.TenderCreateResponse;
import io.codefresh.gradleexample.application.dtos.TenderDto;
import io.codefresh.gradleexample.infrastructure.entity.Tender;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TenderMapper {

    public static TenderDto toTenderDto(@NonNull Tender tender) {
        return new TenderDto(
                tender.getId().toString(),
                tender.getName(),
                tender.getDescription(),
                tender.getServiceType(),
                tender.getStatus(),
                tender.getOrganizationId().toString(),
                tender.getVersion(),
                tender.getCreatedAt()
        );
    }

    public static List<TenderDto> toTenderDtoList(List<Tender> tenders) {
        return tenders.stream()
                .map(TenderMapper::toTenderDto).toList();
    }

    public static Tender toTender(@NonNull TenderCreateRequest request,
                                  @NonNull String organizationId,
                                  @NonNull String status) {
        Tender tender = new Tender();
        tender.setName(request.tenderName());
        tender.setDescription(request.tenderDescription());
        tender.setStatus(status);
        tender.setOrganizationId(UUID.fromString(organizationId));
        tender.setServiceType(request.tenderServiceType());
        tender.setVersion(1);
        tender.setCreatedAt(LocalDateTime.now());
        tender.setUpdatedAt(LocalDateTime.now());
        return tender;
    }

    public static TenderCreateResponse toTenderCreateResponse(Tender tender) {
        return new TenderCreateResponse(
                tender.getId().toString(),
                tender.getName(),
                tender.getDescription(),
                tender.getServiceType(),
                tender.getStatus(),
                tender.getOrganizationId().toString(),
                tender.getVersion(),
                tender.getCreatedAt()
        );
    }
}
