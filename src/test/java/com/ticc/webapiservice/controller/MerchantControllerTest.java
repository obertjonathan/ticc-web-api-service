package com.ticc.webapiservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.ticc.webapiservice.dto.response.EventResponseDTO;
import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.service.MerchantAuthService;
import com.ticc.webapiservice.service.MerchantService;

@WithMockUser
@ExtendWith(MockitoExtension.class)
@WebMvcTest(MerchantController.class)
public class MerchantControllerTest {

    private final String USERNAME = "username";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MerchantService merchantService;

    @MockBean
    private MerchantAuthService merchantAuthService;

    private MerchantResponseDTO merchantResponseDTO;

    private String jwtToken;

    @BeforeEach
    void setUp()
    {
        merchantResponseDTO = new MerchantResponseDTO();
        merchantResponseDTO.setId(UUID.randomUUID().toString());
        merchantResponseDTO.setName("name");
        merchantResponseDTO.setUsername("username");
        merchantResponseDTO.setEmail("email@mail.com");
        merchantResponseDTO.setLocation("location");

        jwtToken = obtainJwtToken();
    }

    private String obtainJwtToken() {
        return "Bearer fake-token";
    }

    @Test
    void testGetMerchantEvent() throws Exception {
        List<EventResponseDTO> eventResponseDTO = List.of(new EventResponseDTO());

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(merchantService.getEventByMerchant(any())).thenReturn(eventResponseDTO);

        mockMvc.perform(get("/api/v1/merchants/" + USERNAME + "/events")
        .header("Authorization", jwtToken)
        .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("00"))
        .andExpect(jsonPath("$.data").isArray());

    }
    
}
