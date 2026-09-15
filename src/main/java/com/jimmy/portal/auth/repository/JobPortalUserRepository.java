package com.jimmy.portal.auth.repository;

import com.jimmy.portal.auth.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPortalUserRepository extends JpaRepository<JobPortalUser, Long> {

    Optional<JobPortalUser> findByEmailOrMobileNumber(String email, String mobileNumber);

    Optional<JobPortalUser> findByEmail(String email);

}
