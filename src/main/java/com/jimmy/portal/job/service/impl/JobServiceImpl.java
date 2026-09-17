package com.jimmy.portal.job.service.impl;

import com.jimmy.portal.auth.entity.JobPortalUser;
import com.jimmy.portal.auth.repository.JobPortalUserRepository;
import com.jimmy.portal.company.dto.JobDto;
import com.jimmy.portal.company.entity.Job;
import com.jimmy.portal.job.dto.JobApplicationDto;
import com.jimmy.portal.job.dto.UpdateJobApplicationDto;
import com.jimmy.portal.job.entity.JobApplication;
import com.jimmy.portal.job.repository.JobApplicationRepository;
import com.jimmy.portal.job.repository.JobRepository;
import com.jimmy.portal.job.service.IJobService;
import com.jimmy.portal.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobServiceImpl implements IJobService {

    private final JobRepository jobRepository;
    private final JobPortalUserRepository userRepository;
    private final JobApplicationRepository jobApplicationRepository;

    @Override
    public List<JobDto> getEmployerJobs(String employerEmail) {
        JobPortalUser employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }

        List<Job> jobs = employer.getCompany().getJobs();
        return jobs.stream()
                .map(ApplicationUtility::transformJobToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public JobDto updateJobStatus(Long jobId, String status, String employerEmail) {
        // Validate status
        if (!status.equals("ACTIVE") && !status.equals("CLOSED") && !status.equals("DRAFT")) {
            throw new RuntimeException("Invalid status. Must be ACTIVE, CLOSED, or DRAFT");
        }
        JobPortalUser employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }
        Job job = employer.getCompany().getJobs().stream().filter(j -> j.getId().equals(jobId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(status);
        return ApplicationUtility.transformJobToDto(job);
    }

    @Override
    @Transactional
    public JobDto createJob(JobDto jobDto, String employerEmail) {
        // Validate employer and get their company
        JobPortalUser employer = userRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned. Please contact admin.");
        }
        Job job = tranformDtoToEntity(jobDto);
        job.setPostedDate(Instant.now());
        job.setApplicationsCount(0);
        job.setStatus("DRAFT");
        job.setCompany(employer.getCompany());
        Job savedJob = jobRepository.save(job);
        return ApplicationUtility.transformJobToDto(savedJob);
    }

    @Override
    public List<JobApplicationDto> getApplicationsByJobForEmployer(Long jobId) {
        List<JobApplication> applications = jobApplicationRepository.findByJobIdOrderByAppliedAtAsc(jobId);
        return applications.stream()
                .map(ApplicationUtility::mapToJobApplicationDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean updateJobApplication(UpdateJobApplicationDto dto) {
        int updatedRows = jobApplicationRepository.updateStatusAndNotesById(
                dto.status().name(), dto.notes(),dto.applicationId(), ApplicationUtility.getLoggedInUser());
        return updatedRows > 0;
    }

    private Job tranformDtoToEntity(JobDto jobDto) {
        Job job = new Job();
        BeanUtils.copyProperties(jobDto, job);
        return job;
    }
}