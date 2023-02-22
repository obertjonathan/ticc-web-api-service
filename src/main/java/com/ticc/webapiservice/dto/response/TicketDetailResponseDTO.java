package com.ticc.webapiservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TicketDetailResponseDTO {
    private Long id;
    private Long eventId;
    private String name;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    @JsonProperty("tiers")
    private List<TierResponseDTO> tierResponses;
}
