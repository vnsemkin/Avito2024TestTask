package io.codefresh.gradleexample.infrastructure.dao;

import io.codefresh.gradleexample.infrastructure.entity.BidFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BidFeedbackJpaRepository extends JpaRepository<BidFeedback, UUID> {


}
