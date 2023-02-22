package com.ticc.webapiservice.controller;

import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.dto.response.TransactionResponseDTO;
import com.ticc.webapiservice.dto.response.TransactionTicketResponseDTO;
import com.ticc.webapiservice.entity.Merchant;
import com.ticc.webapiservice.entity.Tier;
import com.ticc.webapiservice.service.MerchantAuthService;
import com.ticc.webapiservice.service.TierService;
import com.ticc.webapiservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@ExtendWith(MockitoExtension.class)
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @MockBean
    TransactionService transactionService;
    @MockBean
    MerchantAuthService merchantAuthService;
    @MockBean
    TierService tierService;
    @Mock
    Tier tier;
    @Mock
    Merchant merchant;
    @Autowired
    private MockMvc mockMvc;
    MerchantResponseDTO merchantResponseDTO;
    private String jwtToken;

    @BeforeEach
    public void setUp() {
        merchantResponseDTO = new MerchantResponseDTO();
        merchantResponseDTO.setId("merchant1");
        merchantResponseDTO.setName("merchant");
        merchantResponseDTO.setUsername("username");
        merchantResponseDTO.setEmail("email@mail.com");
        merchantResponseDTO.setBio("bio");
        merchantResponseDTO.setLocation("location");

        jwtToken = obtainJwtToken();
    }

    private String obtainJwtToken() {
        return "Bearer fake-token";
    }

    @Test
    void getTransactionTickets() throws Exception {
        TransactionTicketResponseDTO transactionTicketResponseDTO = new TransactionTicketResponseDTO();
        transactionTicketResponseDTO.setTransactionId("transaction1");
        transactionTicketResponseDTO.setTierName("tier name");

        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setTransactionTickets(List.of(transactionTicketResponseDTO));

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(tierService.findById(1L)).thenReturn(Optional.of(tier));
        when(merchant.getUsername()).thenReturn("username");
        when(transactionService.getTransactionDetails("transaction1")).thenReturn(List.of(responseDTO));


        mockMvc.perform(get("/api/v1/transactions/" + merchant.getUsername())
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00"));
    }

    @Test
    void getTransactionTicketsNotValid() throws Exception {
        TransactionTicketResponseDTO transactionTicketResponseDTO = new TransactionTicketResponseDTO();
        transactionTicketResponseDTO.setTransactionId("transaction1");
        transactionTicketResponseDTO.setTierName("tier name");

        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setTransactionTickets(List.of(transactionTicketResponseDTO));

        when(merchantAuthService.decodeToken(any())).thenReturn(merchantResponseDTO);
        when(tierService.findById(1L)).thenReturn(Optional.of(tier));
        when(merchant.getUsername()).thenReturn("test");
        when(transactionService.getTransactionDetails("transaction1")).thenReturn(List.of(responseDTO));


        mockMvc.perform(get("/api/v1/transactions/" + merchant.getUsername())
                        .header("Authorization", jwtToken)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("14"));
    }
}