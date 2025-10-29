package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.companyInfo.CompanyInfo;
import com.geobudget.geobudget.validator.InnValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CompanyInfoServiceTest {
    private DadataService dadataService;
    private InnValidator innValidator;
    private CompanyInfoService service;

    @BeforeEach
    void setUp() {
        dadataService = Mockito.mock(DadataService.class);
        innValidator = new InnValidator();
        service = new CompanyInfoService(dadataService, innValidator);
    }

    @Test
    void getCompanyByInn_delegatesToDadata() throws Exception {
        CompanyInfo info = new CompanyInfo();
        when(dadataService.getCompanyByInn("1234567890")).thenReturn(info);
        CompanyInfo result = service.getCompanyByInn("1234567890");
        assertSame(info, result);
    }
}


