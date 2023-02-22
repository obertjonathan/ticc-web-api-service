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
public class EventDetailResponseDTO {
    private Long id;
    private String merchantUsername;
    private Long categoryId;
    private String name;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate sellStartDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate sellEndDate;
    private String description;
    private String location;
    @JsonProperty("tickets")
    private List<TicketDetailResponseDTO> ticketResponses;

}
