package com.jimmy.portal.job.repository;


import com.jimmy.portal.job.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    // Delete an application by user ID and job ID
    void deleteByUserIdAndJobId(Long userId, Long jobId);

    // Find all applications by user ID
    List<JobApplication> findByUserIdOrderByAppliedAtDesc(Long userId);

    // Find applications by job ID
    List<JobApplication> findByJobIdOrderByAppliedAtAsc(Long jobId);

    @Modifying
    @Query("UPDATE JobApplication j SET j.status = :status, j.notes = :notes, j.updatedAt = CURRENT_TIMESTAMP, j.updatedBy = :updatedBy WHERE j.id = :id")
    int updateStatusAndNotesById(@Param("status") String status, @Param("notes") String notes,
                                 @Param("id") Long id, @Param("updatedBy") String updatedBy);
}