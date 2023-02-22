package com.ticc.webapiservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TicketResponseDTO {
    private Long id;
    private Long eventId;
    private String name;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
}
