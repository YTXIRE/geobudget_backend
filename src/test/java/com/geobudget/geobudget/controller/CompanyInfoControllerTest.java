package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.companyInfo.CompanyInfo;
import com.geobudget.geobudget.service.CompanyInfoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyInfoController.class)
class CompanyInfoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyInfoService companyInfoService;

    @Test
    void getCompanyByInn_returns200() throws Exception {
        Mockito.when(companyInfoService.getCompanyByInn(anyString())).thenReturn(new CompanyInfo());
        mockMvc.perform(get("/api/v1/company-info/get-company-by-inn").param("inn", "1234567890"))
                .andExpect(status().isOk());
    }
}


