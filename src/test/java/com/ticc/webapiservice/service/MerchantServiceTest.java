package com.ticc.webapiservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ticc.webapiservice.exception.exts.merchant.MerchantNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ticc.webapiservice.dto.request.MerchantAddRequestDTO;
import com.ticc.webapiservice.dto.response.EventResponseDTO;
import com.ticc.webapiservice.entity.Event;
import com.ticc.webapiservice.entity.Merchant;
import com.ticc.webapiservice.repository.MerchantRepository;


@SpringBootTest
public class MerchantServiceTest {
    
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MerchantService merchantService;

    @MockBean
    private MerchantRepository merchantRepository;

    @Test
    void testAddMerchant() {
        Merchant merchant = new Merchant();
        merchant.setId((UUID.randomUUID()).toString());
        merchant.setEmail("email@mail.com");
        merchant.setUsername("username");
        merchant.setPassword("Password1@");
        merchant.setName("name");
        merchant.setBio("bio");
        merchant.setLocation("location");

        Mockito.when(merchantRepository.save(merchant)).thenReturn(merchant);
        assertEquals(merchant.getUsername(), merchantService.addMerchant(convertToAddDTO(merchant)).getUsername());
    }

    @Test
    void merchantNotFound()
    {
        Merchant merchant = new Merchant();
        merchant.setId((UUID.randomUUID()).toString());
        merchant.setEmail("email@mail.com");
        merchant.setUsername("username");
        merchant.setPassword("Password1@");
        merchant.setName("name");
        merchant.setBio("bio");
        merchant.setLocation("location");

        MerchantNotFoundException notFoundException = assertThrows(MerchantNotFoundException.class,
        ()->merchantService.getMerchantByUsername("user12"));

        assertEquals("Merchant with specific username not found", notFoundException.getMessage());
    }

    private MerchantAddRequestDTO convertToAddDTO(Merchant merchant) {
        return modelMapper.map(merchant, MerchantAddRequestDTO.class);
    }

    @Test
    void testGetEventByMerchant() {
         // create a sample merchant and events
         Merchant merchant = new Merchant();
         merchant.setId(UUID.randomUUID().toString());
         merchant.setUsername("john_doe");
 
         Event event1 = new Event();
         event1.setId(1L);
         event1.setName("Event 1");
         event1.setMerchant(merchant);
 
         Event event2 = new Event();
         event2.setId(2L);
         event2.setName("Event 2");
         event2.setMerchant(merchant);
 
         merchant.setEvents(List.of(event1, event2));
 
         // mock the repository method
         when(merchantRepository.findByUsername(any())).thenReturn(Optional.of(merchant));
 
         // call the service method
         List<EventResponseDTO> eventResponseDTOs = merchantService.getEventByMerchant("john_doe");
 
         // assert the result
         assertEquals(2, eventResponseDTOs.size());
         assertEquals("Event 1", eventResponseDTOs.get(0).getName());
         assertEquals("Event 2", eventResponseDTOs.get(1).getName());

    }

    @Test
    void testGetMerchantByUsername() {
        Merchant merchant = new Merchant();
        merchant.setId((UUID.randomUUID()).toString());
        merchant.setEmail("email@mail.com");
        merchant.setUsername("username");
        merchant.setPassword("Password1@");
        merchant.setName("name");
        merchant.setBio("bio");
        merchant.setLocation("location");

        Mockito.when(merchantRepository.findByUsername("username")).thenReturn(Optional.of(merchant));
        assertEquals(merchant.getEmail(), merchantService.getMerchantByUsername("username").getEmail());

    }
}
