package com.ticc.webapiservice.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionTicketResponseDTO {
    private String transactionId;
    private String tierName;
    private Integer purchasedQty;
    private Long tierTotalPrice;
}
