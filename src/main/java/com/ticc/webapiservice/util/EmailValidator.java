package com.ticc.webapiservice.util;

import java.util.regex.Pattern;

public class EmailValidator {
    private EmailValidator() {}

    public static boolean validateEmail(String email) {
        String regex = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}";
        Pattern p = Pattern.compile(regex);

        if (email == null) {
            return false;
        }
        return p.matcher(email).matches();
    }

}
