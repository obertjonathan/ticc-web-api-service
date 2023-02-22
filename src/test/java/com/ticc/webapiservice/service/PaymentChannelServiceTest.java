package com.ticc.webapiservice.service;

import com.ticc.webapiservice.dto.response.PaymentChannelResponseDTO;
import com.ticc.webapiservice.entity.PaymentChannel;
import com.ticc.webapiservice.exception.exts.paymentchannel.PaymentChannelNotFoundException;
import com.ticc.webapiservice.repository.PaymentChannelRepository;
import com.ticc.webapiservice.service.implementation.PaymentChannelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentChannelServiceTest {
    @Mock
    PaymentChannelRepository paymentChannelRepository;
    @InjectMocks
    PaymentChannelServiceImpl paymentChannelService;
    @Mock
    PaymentChannel paymentChannel;
    ModelMapper modelMapper = Mockito.spy(ModelMapper.class);
    PaymentChannelResponseDTO falseResponse;
    PaymentChannelResponseDTO trueResponse;

    @BeforeEach
    void setUp() {
        paymentChannelService = new PaymentChannelServiceImpl(paymentChannelRepository, modelMapper);

        trueResponse = new PaymentChannelResponseDTO();
        trueResponse.setId(1L);
        trueResponse.setChannelType("channel type");
        trueResponse.setName("payment name");
        trueResponse.setStatus(true);

        falseResponse = new PaymentChannelResponseDTO();
        falseResponse.setId(1L);
        falseResponse.setChannelType("channel type");
        falseResponse.setName("payment name");
        falseResponse.setStatus(false);
    }

    @Test
    void testGetPaymentChannels() {
        when(paymentChannelRepository.findAll()).thenReturn(List.of(paymentChannel));
        when(paymentChannelRepository.count()).thenReturn(1L);

        List<PaymentChannelResponseDTO> responseDTO = paymentChannelService.getPaymentChannels();

        assertNotNull(responseDTO);
    }

    @Test
    void testUpdateStatusTrue() {
        when(paymentChannelRepository.findById(any())).thenReturn(Optional.of(paymentChannel));
        when(paymentChannel.getStatus()).thenReturn(true);
        when(paymentChannelRepository.save(any())).thenReturn(paymentChannel);

        PaymentChannelResponseDTO responseDTO = paymentChannelService.updateStatus(any());

        assertEquals(responseDTO.getStatus(), trueResponse.getStatus());
    }

    @Test
    void testUpdateStatusFalse() {
        when(paymentChannelRepository.findById(any())).thenReturn(Optional.of(paymentChannel));
        when(paymentChannel.getStatus()).thenReturn(false);
        when(paymentChannelRepository.save(any())).thenReturn(paymentChannel);

        PaymentChannelResponseDTO responseDTO = paymentChannelService.updateStatus(any());

        assertEquals(responseDTO.getStatus(), falseResponse.getStatus());
    }

    @Test
    void testUpdateStatusNotFound() {
        when(paymentChannelRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(PaymentChannelNotFoundException.class, () -> paymentChannelService.updateStatus(any()));
    }

    @Test
    void testGetPaymentChannelsNotFound() {
        when(paymentChannelRepository.findAll()).thenReturn(List.of(paymentChannel));
        assertThrows(PaymentChannelNotFoundException.class, () -> paymentChannelService.getPaymentChannels());
    }
}