package com.ticc.webapiservice.service.implementation;

import com.ticc.webapiservice.dto.response.TransactionResponseDTO;
import com.ticc.webapiservice.dto.response.TransactionTicketResponseDTO;
import com.ticc.webapiservice.entity.*;
import com.ticc.webapiservice.repository.TransactionRepository;
import com.ticc.webapiservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<TransactionResponseDTO> getTransactionDetails(String username) {
        return transactionRepository.findAll().stream().filter(e -> {
            Event event = e.getTransactionTickets().get(0).getTier().getTicket().getEvent();
            if (event.getMerchant().getUsername().equalsIgnoreCase(username)) return true;
            return false;
        }).map(this::convertToDetailDto).toList();
    }

    private TransactionTicketResponseDTO convertToDto(TransactionTicket transactionTicket) {
        return modelMapper.map(transactionTicket, TransactionTicketResponseDTO.class);
    }

    private TransactionResponseDTO convertToDetailDto(Transactions transactions) {
        TransactionResponseDTO transactionResponseDTO = modelMapper.map(transactions, TransactionResponseDTO.class);

        // Set relational mapper
        Merchant merchant = transactions.getTransactionTickets().get(0).getTier().getTicket().getEvent().getMerchant();
        transactionResponseDTO.setUserName(transactions.getUser().getName());
        transactionResponseDTO.setMerchantName(merchant.getName());
        transactionResponseDTO.setEventName(transactions.getTransactionTickets().get(0).getTier().getTicket().getEvent().getName());
        transactionResponseDTO.setCategoryName(transactions.getTransactionTickets().get(0).getTier().getTicket().getEvent().getCategory().getName());
        transactionResponseDTO.setTicketName(transactions.getTransactionTickets().get(0).getTier().getTicket().getName());

        // Set relational mapper for tickets
        List<TransactionTicket> transactionTickets = transactions.getTransactionTickets();
        transactionResponseDTO.setTransactionTickets(transactionTickets.stream().map(this::convertToDto).toList());

        return transactionResponseDTO;
    }

}
