package com.ticc.webapiservice.service;

import com.ticc.webapiservice.dto.response.MerchantResponseDTO;
import com.ticc.webapiservice.entity.Merchant;
import com.ticc.webapiservice.repository.MerchantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class MerchantAuthServiceTest {
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MerchantRepository merchantRepository;
    @Autowired
    private MerchantAuthService merchantAuthService;
    @Autowired
    private MerchantService merchantService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateAndGenerated() {
        String username = "testuser";
        String password = "testpassword";
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userDetails.getPassword()).thenReturn("encodedpassword");
        when(passwordEncoder.matches(password, "encodedpassword")).thenReturn(true);
        String result = merchantAuthService.validateAndGenerated(username, password);
        assertNotNull(result);

    }

    @Test
    void decodeToken() {
        // create a mock Merchant instance with the username "my-username"
        Merchant merchant = Merchant.builder()
                .id("1")
                .username("my-username")
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        // mock the behavior of merchantRepository.findByUsername()
        when(merchantRepository.findByUsername("my-username")).thenReturn(Optional.of(merchant));

        // call getMerchantByUsername() and check if it returns the expected Merchant instance
        MerchantResponseDTO expectedResponse = new MerchantResponseDTO();
        expectedResponse.setId("1");
        expectedResponse.setUsername("my-username");
        expectedResponse.setName("John Doe");
        expectedResponse.setEmail("john.doe@example.com");

        MerchantResponseDTO actualResponse = merchantService.getMerchantByUsername("my-username");
        assertEquals(expectedResponse, actualResponse);
    }
}