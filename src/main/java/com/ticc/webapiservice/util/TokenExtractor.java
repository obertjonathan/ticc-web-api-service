package com.ticc.webapiservice.util;

import org.springframework.http.HttpHeaders;

import java.util.List;

public class TokenExtractor {
    private TokenExtractor() {
    }

    public static String extractToken(HttpHeaders headers) {
        String token = "";
        if (headers.containsKey("authorization")) {
            List<String> authHeader = headers.get("authorization");
            if (authHeader != null && !authHeader.isEmpty())
                token = authHeader.get(0).substring(7);
        }
        return token;
    }

}
