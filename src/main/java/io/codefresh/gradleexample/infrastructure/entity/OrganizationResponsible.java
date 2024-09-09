package io.codefresh.gradleexample.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "organization_responsible")
public class OrganizationResponsible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @ForeignKey(name = "FK_ORGANIZATION"))
    private Organization organization;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_EMPLOYEE"))
    private Employee employee;
}
