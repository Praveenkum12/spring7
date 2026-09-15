package com.jimmy.portal.company.service.impl;

import com.jimmy.portal.company.dto.CompanyDto;
import com.jimmy.portal.company.mapper.CompanyMapper;
import com.jimmy.portal.company.repository.CompanyRepository;
import com.jimmy.portal.company.entity.Company;
import com.jimmy.portal.company.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyMapper companyMapper;

    private final CompanyRepository companyRepository;

    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().stream().map(companyMapper::toDto).toList();
    }

    @Override
    public List<CompanyDto> getAllCompaniesForAdmin() {
        List<Company> companyList =companyRepository.findAll();
        return companyList.stream().map(companyMapper::companyToDtoWithJobListNull).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteCompanyById(Long id) {
        companyRepository.deleteById(id);
    }

    @Transactional
    @Override
    public boolean updateCompanyDetails(Long id, CompanyDto companyDto) {
        int updatedRecords = companyRepository.updateCompanyDetails(
                id,companyDto.name(),companyDto.logo(),
                companyDto.industry(),companyDto.size(),companyDto.rating(),
                companyDto.locations(),companyDto.founded(),companyDto.description(),
                companyDto.employees(),companyDto.website()
        );
        return updatedRecords > 0;
    }

    @Transactional
    @Override
    public boolean createCompany(CompanyDto companyDto) {
        Company company = companyMapper.toEntity(companyDto);
        Company savedCompany = companyRepository.save(company);
        return savedCompany.getId() != null && savedCompany.getId() > 0;
    }


}
