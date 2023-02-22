package com.ticc.webapiservice.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MerchantResponseDTO {
    
    private String id;
    private String email;
    private String username;
    private String name;
    private String bio;
    private String location;
}
