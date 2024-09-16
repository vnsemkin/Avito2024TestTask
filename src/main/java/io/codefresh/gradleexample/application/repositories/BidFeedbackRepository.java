package io.codefresh.gradleexample.application.repositories;

import io.codefresh.gradleexample.infrastructure.dao.BidFeedbackJpaRepository;
import io.codefresh.gradleexample.infrastructure.entity.BidFeedback;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BidFeedbackRepository extends BidFeedbackJpaRepository {
    @Query("SELECT b FROM BidFeedback b WHERE b.authorId = :username")
    Optional<BidFeedback> findByAuthorId(@NonNull UUID username);
}
