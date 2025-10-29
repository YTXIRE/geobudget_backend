package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.companyInfo.CompanyInfo;
import com.geobudget.geobudget.validator.InnValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyInfoService {
    private final DadataService dadataService;
    private final InnValidator innValidator;

    public CompanyInfo getCompanyByInn(String inn) throws Exception {
        innValidator.validateInn(inn);
        return dadataService.getCompanyByInn(inn);
    }
}


