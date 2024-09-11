package io.codefresh.gradleexample.infrastructure.dao;

import io.codefresh.gradleexample.infrastructure.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeJpaRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);
}
