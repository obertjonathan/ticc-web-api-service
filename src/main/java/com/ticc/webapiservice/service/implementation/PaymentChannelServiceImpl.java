package com.ticc.webapiservice.service.implementation;

import com.ticc.webapiservice.dto.response.PaymentChannelResponseDTO;
import com.ticc.webapiservice.entity.PaymentChannel;
import com.ticc.webapiservice.exception.exts.paymentchannel.PaymentChannelNotFoundException;
import com.ticc.webapiservice.repository.PaymentChannelRepository;
import com.ticc.webapiservice.service.PaymentChannelService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentChannelServiceImpl implements PaymentChannelService {
    private final PaymentChannelRepository paymentChannelRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PaymentChannelResponseDTO> getPaymentChannels() {
        List<PaymentChannel> paymentChannels = paymentChannelRepository.findAll();
        if (paymentChannelRepository.count() != 0) {
            return paymentChannels.stream().map(this::convertToDto).toList();
        }
        throw new PaymentChannelNotFoundException("No payment channels found");
    }

    @Override
    public PaymentChannelResponseDTO updateStatus(Long id) {
        PaymentChannel paymentChannel = paymentChannelRepository.findById(id).orElseThrow(() -> new PaymentChannelNotFoundException("No payment channels found"));

        if (paymentChannel.getStatus()) paymentChannel.setStatus(false);
        else paymentChannel.setStatus(true);

        return convertToDto(paymentChannelRepository.save(paymentChannel));
    }

    private PaymentChannelResponseDTO convertToDto(PaymentChannel paymentChannel) {
        return modelMapper.map(paymentChannel, PaymentChannelResponseDTO.class);
    }
}
