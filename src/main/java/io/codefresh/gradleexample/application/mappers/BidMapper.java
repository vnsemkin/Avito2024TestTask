package io.codefresh.gradleexample.application.mappers;

import io.codefresh.gradleexample.application.config.TenderBidStatus;
import io.codefresh.gradleexample.application.dtos.BidCreateReq;
import io.codefresh.gradleexample.application.dtos.BidDto;
import io.codefresh.gradleexample.infrastructure.entity.Bid;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BidMapper {

    public static BidDto toBidDto(@NonNull Bid bid) {
        return new BidDto(
                bid.getBidId().toString(),
                bid.getName(),
                bid.getDescription(),
                bid.getStatus(),
                bid.getTenderId().toString(),
                bid.getAuthorType(),
                bid.getAuthorId().toString(),
                bid.getVersion(),
                bid.getCreatedAt()
        );
    }

    public static List<BidDto> toBidDtoList(@NonNull List<Bid> bids) {
        return bids.stream().map(BidMapper::toBidDto).toList();
    }

    public static Bid createBid(@NonNull BidCreateReq request) {
        Bid bid = new Bid();
        bid.setBidId(UUID.randomUUID());
        bid.setName(request.name());
        bid.setDescription(request.description());
        bid.setStatus(TenderBidStatus.CREATED.getValue());
        bid.setTenderId(UUID.fromString(request.tenderId()));
        bid.setAuthorType(request.authorType());
        bid.setAuthorId(UUID.fromString(request.authorId()));
        bid.setVersion(1);
        bid.setCreatedAt(LocalDateTime.now());
        bid.setUpdatedAt(LocalDateTime.now());
        return bid;
    }
}
