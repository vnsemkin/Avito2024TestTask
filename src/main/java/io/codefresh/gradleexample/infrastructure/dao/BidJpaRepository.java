package io.codefresh.gradleexample.infrastructure.dao;

import io.codefresh.gradleexample.infrastructure.entity.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BidJpaRepository extends JpaRepository<Bid, UUID> {
    Page<Bid> findAllByAuthorId(UUID id, Pageable pageRequest);
}
