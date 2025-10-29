package com.geobudget.geobudget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geobudget.geobudget.dto.auth.LoginDTO;
import com.geobudget.geobudget.dto.auth.RegisterDTO;
import com.geobudget.geobudget.dto.auth.RegisterResponseDTO;
import com.geobudget.geobudget.dto.jwt.JwtAuthenticationDTO;
import com.geobudget.geobudget.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void register_returns201() throws Exception {
        Mockito.when(authService.register(any(RegisterDTO.class)))
                .thenReturn(RegisterResponseDTO.builder().username("john").email("john@example.com").build());

        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("john");
        dto.setPassword("pass");
        dto.setRepeatPassword("pass");
        dto.setEmail("john@example.com");
        dto.setPhone("+7");
        dto.setCity("Moscow");
        dto.setCountryId(1L);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void login_returns200() throws Exception {
        Mockito.when(authService.login(any(LoginDTO.class)))
                .thenReturn(new JwtAuthenticationDTO());

        LoginDTO dto = new LoginDTO();
        dto.setUsername("john");
        dto.setPassword("pass");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}


