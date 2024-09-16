package io.codefresh.gradleexample.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "bid_feedback")
public class BidFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "bid_id", nullable = false)
    private UUID bidId;

    @Column(name = "feedback", nullable = false, length = 1000)
    private String feedback;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;
}
