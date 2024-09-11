package io.codefresh.gradleexample.infrastructure.dao;

import io.codefresh.gradleexample.infrastructure.entity.Tender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TenderJpaRepository extends JpaRepository<Tender, UUID> {

    @Query("SELECT t FROM Tender t WHERE t.serviceType IN (:serviceTypes) AND t.status = :status")
    Page<Tender> findAllByServiceTypes(@Param("serviceTypes") List<String> serviceTypes,
                                       @Param("status") String status, Pageable pageable);

    Page<Tender> findAllByEmployeeId(UUID id, Pageable pageable);

    @Query("SELECT t FROM Tender t WHERE t.status = :status")
    Page<Tender> findAllWithStatusPublished(@Param("status") String status, PageRequest pageRequest);
}
