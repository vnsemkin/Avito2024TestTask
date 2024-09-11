package io.codefresh.gradleexample.infrastructure.dao;

import io.codefresh.gradleexample.infrastructure.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationJpaRepository extends JpaRepository<Organization, Long> {
}
