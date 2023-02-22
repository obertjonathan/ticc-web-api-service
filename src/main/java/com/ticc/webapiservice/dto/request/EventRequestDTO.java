package com.ticc.webapiservice.dto.request;

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
public class EventRequestDTO {
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
}
