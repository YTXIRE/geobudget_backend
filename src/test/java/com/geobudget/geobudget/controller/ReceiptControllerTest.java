package com.geobudget.geobudget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geobudget.geobudget.dto.checkReceipt.CheckReceipt;
import com.geobudget.geobudget.service.CheckReceiptService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReceiptController.class)
class ReceiptControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CheckReceiptService checkReceiptService;

    @Test
    void checkReceipt_returns200() throws Exception {
        Mockito.when(checkReceiptService.checkReceipt(anyString()))
                .thenReturn(new CheckReceipt());

        mockMvc.perform(post("/api/v1/receipt/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("qr", "t=..."))))
                .andExpect(status().isOk());
    }
}


