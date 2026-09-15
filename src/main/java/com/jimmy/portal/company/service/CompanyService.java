package com.jimmy.portal.company.service;

import com.jimmy.portal.company.repository.CompanyRepository;
import com.jimmy.portal.company.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }


}
