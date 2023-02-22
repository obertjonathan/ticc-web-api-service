package com.ticc.webapiservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionResponseDTO {
    private String id;
    private String userName;
    private String merchantName;
    private String eventName;
    private String categoryName;
    private String ticketName;
    private Integer totalQty;
    private Long totalPrice;
    private String paymentStatus;
    private LocalDateTime purchasedDate;
    private String invoiceId;
    @JsonProperty("transaction_tickets")
    private List<TransactionTicketResponseDTO> transactionTickets;
}
