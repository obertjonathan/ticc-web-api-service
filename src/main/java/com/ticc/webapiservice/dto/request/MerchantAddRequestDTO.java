package com.ticc.webapiservice.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MerchantAddRequestDTO {
    
    private String email;
    private String username;
    private String password;
    private String name;
    private String bio;
    private String location;
}
