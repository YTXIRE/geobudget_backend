package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.CategoryDto;
import com.geobudget.geobudget.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    void getCategoryByInn_returns200() throws Exception {
        Mockito.when(categoryService.getCategory(anyString()))
                .thenReturn(CategoryDto.builder().id(1L).name("Прочее").build());

        mockMvc.perform(get("/api/v1/category/get-category-by-inn").param("inn", "1234567890"))
                .andExpect(status().isOk());
    }
}


