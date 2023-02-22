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
public class TierResponseDTO {
    private Long id;
    private Long ticketId;
    private String name;
    private String description;
    private Long price;
    private Integer qty;
}
