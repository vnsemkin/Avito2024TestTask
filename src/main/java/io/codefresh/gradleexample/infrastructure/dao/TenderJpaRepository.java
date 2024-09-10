package io.codefresh.gradleexample.infrastructure.dao;

import io.codefresh.gradleexample.infrastructure.entity.Tender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TenderJpaRepository extends JpaRepository<Tender, UUID> {

    @Query("SELECT t FROM Tender t WHERE t.serviceType IN (:serviceTypes)")
    Page<Tender> findAllByServiceTypes(@Param("serviceTypes") List<String> serviceTypes, Pageable pageable);
}
