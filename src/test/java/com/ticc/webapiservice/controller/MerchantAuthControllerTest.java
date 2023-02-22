package com.ticc.webapiservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticc.webapiservice.dto.request.MerchantAddRequestDTO;
import com.ticc.webapiservice.dto.request.MerchantRequestDTO;
import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.service.MerchantAuthService;
import com.ticc.webapiservice.service.MerchantService;

@WithMockUser
@ExtendWith(MockitoExtension.class)
@WebMvcTest(MerchantAuthController.class)
public class MerchantAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MerchantService merchantService;

    @MockBean
    private MerchantAuthService merchantAuthService;

    private MerchantRequestDTO merchantRequestDTO;
    private MerchantAddRequestDTO merchantAddRequestDTO;

    private MerchantResponseDTO merchantResponseDTO;
    
    @BeforeEach
    void setUp()
    {   
        merchantRequestDTO = new MerchantRequestDTO();
        merchantRequestDTO.setUsername("username");
        merchantRequestDTO.setPassword("1@Password");

        merchantAddRequestDTO = new MerchantAddRequestDTO();
        merchantAddRequestDTO.setEmail("email@mail.com");
        merchantAddRequestDTO.setUsername("username");
        merchantAddRequestDTO.setName("name");
        merchantAddRequestDTO.setPassword("1@Password");
        merchantAddRequestDTO.setBio("bio");
        merchantAddRequestDTO.setLocation("location");

        merchantResponseDTO = new MerchantResponseDTO();     
        merchantResponseDTO.setId("id");
        merchantResponseDTO.setEmail("email@mail.com");
        merchantResponseDTO.setUsername("username");
        merchantResponseDTO.setName("name");
        merchantResponseDTO.setBio("bio");
        merchantResponseDTO.setLocation("location");
    }

    @Test
    void testLogin() throws Exception {
        when(merchantAuthService.validateAndGenerated(any(),any())).thenReturn("fake-token");
        when(merchantService.getMerchantByUsername(any())).thenReturn(merchantResponseDTO);

        mockMvc.perform(post("/api/v1/merchants/auth/login")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(merchantRequestDTO))
            .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("00"))
            .andExpect(jsonPath("$.message").value("Success"))
            .andExpect(jsonPath("$.data.token").value("fake-token"))
            .andExpect(jsonPath("$.data.username").value("username"))
            .andExpect(jsonPath("$.data.name").value("name"));
    }

    @Test
    void testLoginFailed() throws Exception
    {
        when(merchantAuthService.validateAndGenerated(any(), any())).thenReturn(null);

        mockMvc.perform(post("/api/v1/merchants/auth/login")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(merchantAddRequestDTO))
            .with(csrf()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("99"))
            .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void testRegister() throws Exception {
        when(merchantService.addMerchant(any())).thenReturn(merchantResponseDTO);

        mockMvc.perform(post("/api/v1/merchants/auth/register")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(merchantAddRequestDTO))
            .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("00"))
            .andExpect(jsonPath("$.message").value("Merchant user with username "+merchantResponseDTO.getUsername()+" registered"))
            .andExpect(jsonPath("$.data.id").value("id"))
            .andExpect(jsonPath("$.data.email").value("email@mail.com"))
            .andExpect(jsonPath("$.data.name").value("name"))
            .andExpect(jsonPath("$.data.username").value("username"))
            .andExpect(jsonPath("$.data.bio").value("bio"))
            .andExpect(jsonPath("$.data.location").value("location"));
    }
}
