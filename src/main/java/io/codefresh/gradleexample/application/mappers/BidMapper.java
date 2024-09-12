package io.codefresh.gradleexample.application.mappers;

import io.codefresh.gradleexample.application.dtos.BidDto;
import io.codefresh.gradleexample.infrastructure.entity.Bid;

import java.util.List;

public class BidMapper {

    public static BidDto toBidDto(Bid bid) {
        return new BidDto(
                bid.getId().toString(),
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

    public static List<BidDto> toBidDtoList(List<Bid> bids) {
        return bids.stream().map(BidMapper::toBidDto).toList();
    }
}
