package com.ticc.webapiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseResponse<T> {
    private String code;
    private String message;
    private T data;

    public static <T> BaseResponse<T> of(String code, String message, T data) {
        return new BaseResponse<>(code,message,data);
    }

    public static <T> BaseResponse<T> of(String code, String message) {
        return new BaseResponse<>(code,message,null);
    }
}
