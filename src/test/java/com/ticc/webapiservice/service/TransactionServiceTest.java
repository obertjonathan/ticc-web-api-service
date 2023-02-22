package com.ticc.webapiservice.service;

import com.ticc.webapiservice.dto.response.TransactionResponseDTO;
import com.ticc.webapiservice.dto.response.TransactionTicketResponseDTO;
import com.ticc.webapiservice.entity.*;
import com.ticc.webapiservice.repository.TransactionRepository;
import com.ticc.webapiservice.service.implementation.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    TransactionRepository transactionRepository;
    @InjectMocks
    TransactionServiceImpl transactionService;
    @Mock
    User user;
    @Mock
    Merchant merchant;
    @Mock
    Event event;
    @Mock
    Category category;
    @Mock
    Ticket ticket;
    @Mock
    Tier tier;
    @Mock
    PaymentChannel paymentChannel;
    @Mock
    Transactions transactions;
    @Mock
    TransactionTicket transactionTicket;
    ModelMapper modelMapper = Mockito.spy(ModelMapper.class);
    TransactionResponseDTO transactionResponseDTO;
    TransactionTicketResponseDTO transactionTicketResponseDTO;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl(transactionRepository, modelMapper);

        merchant.setId("merchant1");
        merchant.setUsername("username");
        merchant.setName("merchant name");

        category.setId(1L);
        category.setName("Music");

        event.setId(1L);
        event.setName("event name");

        ticket.setId(1L);
        ticket.setName("ticket name");

        tier.setId(1L);
        tier.setName("tier name");

        user.setId("user1");
        user.setName("name");

        paymentChannel.setId(1L);
        paymentChannel.setChannelType("channel type");
        paymentChannel.setName("payment name");
        paymentChannel.setStatus(true);

        transactions.setId("tr1");
        transactions.setTotalQty(1);
        transactions.setTotalPrice(10000L);
        transactions.setPaymentStatus("SUCCESS");
        transactions.setPurchasedDate(LocalDateTime.now());
        transactions.setInvoiceId("inv-01");
        transactions.setPaymentChannel(paymentChannel);
        transactions.setTransactionTickets(List.of(transactionTicket));

        transactionTicket.setId("tt1");
        transactionTicket.setTransactions(transactions);
        transactionTicket.setTier(tier);
        transactionTicket.setPurchasedQty(1);
        transactionTicket.setTierTotalPrice(10000L);

        transactionTicketResponseDTO = new TransactionTicketResponseDTO();
        transactionTicketResponseDTO.setTransactionId("tt1");
        transactionTicketResponseDTO.setTierName("tier name");
        transactionTicketResponseDTO.setPurchasedQty(1);
        transactionTicketResponseDTO.setTierTotalPrice(10000L);

        transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setUserName("name");
        transactionResponseDTO.setMerchantName("merchant name");
        transactionResponseDTO.setCategoryName("Music");
        transactionResponseDTO.setEventName("event name");
        transactionResponseDTO.setTicketName("ticket name");
        transactionResponseDTO.setTransactionTickets(List.of(transactionTicketResponseDTO));
    }

    @Test
    void testGetTransactionDetails() {
        when(merchant.getName()).thenReturn("merchant name");
        when(event.getMerchant()).thenReturn(merchant);
        when(event.getName()).thenReturn("event name");
        when(ticket.getEvent()).thenReturn(event);
        when(ticket.getName()).thenReturn("ticket name");
        when(tier.getTicket()).thenReturn(ticket);
        when(tier.getName()).thenReturn("tier name");
        when(transactionTicket.getTier()).thenReturn(tier);
        when(transactions.getUser()).thenReturn(user);
        when(user.getName()).thenReturn("name");

        when(category.getName()).thenReturn("Music");
        when(event.getCategory()).thenReturn(category);
        when(transactionRepository.findAll()).thenReturn(List.of(transactions));
        when(transactions.getId()).thenReturn("tr1");
        when(transactions.getTransactionTickets()).thenReturn(List.of(transactionTicket));
        when(transactionTicket.getId()).thenReturn("tt1");

        when(merchant.getUsername()).thenReturn("username");
        List<TransactionResponseDTO> responseDTO = transactionService.getTransactionDetails("username");

        assertEquals(responseDTO.get(0).getUserName(), transactionResponseDTO.getUserName());
    }
}
