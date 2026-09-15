package com.jimmy.portal.company.controller;

import com.jimmy.portal.company.dto.CompanyDto;
import com.jimmy.portal.company.mapper.CompanyMapper;
import com.jimmy.portal.company.service.CompanyService;
import com.jimmy.portal.company.entity.Company;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CompanyController")
@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

    private final CompanyMapper companyMapper;

    private final CompanyService companyService;

    @Operation(summary = "get all companies")
    @GetMapping(path = "/public", version = "1.0")
    public List<CompanyDto> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return companies.stream().map(companyMapper::toDto).toList();
    }
}
