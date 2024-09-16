package io.codefresh.gradleexample.infrastructure.dao;

import io.codefresh.gradleexample.infrastructure.entity.BidDecision;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BidDecisionJpaRepository extends JpaRepository<BidDecision, UUID> {
    @Query("SELECT b FROM BidDecision b WHERE b.authorId = :username")
    Optional<BidDecision> findByAuthorId(@NonNull UUID username);
}
