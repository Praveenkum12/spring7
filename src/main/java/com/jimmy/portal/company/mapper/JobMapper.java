package com.jimmy.portal.company.mapper;

import com.jimmy.portal.company.dto.JobDto;
import com.jimmy.portal.company.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    @Mapping(source = "company.logo", target = "companyLogo")
    JobDto convertToDto(Job job);

}
