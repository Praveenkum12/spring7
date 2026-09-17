package com.jimmy.portal.job.service;


import com.jimmy.portal.company.dto.JobDto;
import com.jimmy.portal.job.dto.JobApplicationDto;
import com.jimmy.portal.job.dto.UpdateJobApplicationDto;

import java.util.List;

public interface IJobService {

    List<JobDto> getEmployerJobs(String employerEmail);

    JobDto updateJobStatus(Long jobId, String status, String employerEmail);

    JobDto createJob(JobDto jobDto, String employerEmail);

    List<JobApplicationDto> getApplicationsByJobForEmployer(Long jobId);

    boolean updateJobApplication(UpdateJobApplicationDto updateJobApplicationDto);

}