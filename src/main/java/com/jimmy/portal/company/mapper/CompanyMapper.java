package com.jimmy.portal.company.mapper;

import com.jimmy.portal.company.dto.CompanyDto;
import com.jimmy.portal.company.entity.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = JobMapper.class)
public interface CompanyMapper {

    CompanyDto toDto(Company company);

}
