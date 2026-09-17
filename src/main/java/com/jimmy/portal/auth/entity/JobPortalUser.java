package com.jimmy.portal.auth.entity;

import com.jimmy.portal.base.BaseEntity;
import com.jimmy.portal.company.entity.Company;
import com.jimmy.portal.company.entity.Job;
import com.jimmy.portal.job.entity.JobApplication;
import com.jimmy.portal.job.entity.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class JobPortalUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "mobile_number", nullable = false, unique = true)
    private String mobileNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Company company;

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @ManyToMany
    @JoinTable(name = "saved_jobs",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "job_id")})
    private Set<Job> savedJobs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<JobApplication> jobApplications = new LinkedHashSet<>();

}
