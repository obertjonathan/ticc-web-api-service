package com.ticc.webapiservice.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MerchantLoginResponseDTO {
    private String token;
    private String username;
    private String name;

    public static MerchantLoginResponseDTO of(String token, String username,String name)
    {
        return new MerchantLoginResponseDTO(token,username,name);
    }
}
