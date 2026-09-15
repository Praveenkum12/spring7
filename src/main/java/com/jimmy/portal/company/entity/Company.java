package com.jimmy.portal.company.entity;

import com.jimmy.portal.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "logo", length = 500)
    private String logo;

    @Column(name = "industry", nullable = false, length = 100)
    private String industry;

    @Column(name = "size", nullable = false, length = 50)
    private String size;

    @Column(name = "rating", nullable = false, precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(name = "locations", length = 1000)
    private String locations;

    @Column(name = "founded", nullable = false)
    private Integer founded;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "employees")
    private Integer employees;

    @Column(name = "website", length = 500)
    private String website;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private List<Job> jobs;

}