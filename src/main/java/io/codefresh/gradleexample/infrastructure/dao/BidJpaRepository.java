package io.codefresh.gradleexample.infrastructure.dao;

import io.codefresh.gradleexample.infrastructure.entity.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BidJpaRepository extends JpaRepository<Bid, UUID> {
    Page<Bid> findAllByAuthorId(UUID id, Pageable pageRequest);

    @Query("SELECT b FROM Bid b WHERE b.tenderId = :tenderId")
    Page<Bid> findAllByTenderId(@Param("tenderId") UUID id, Pageable pageRequest);

    @Query("SELECT b FROM Bid b WHERE b.bidId = :uuid ORDER BY b.version DESC limit 1" )
    Optional<Bid> findByBidId(UUID uuid);
}
