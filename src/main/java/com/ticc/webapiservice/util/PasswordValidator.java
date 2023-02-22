package com.ticc.webapiservice.util;

import java.util.regex.Pattern;

public class PasswordValidator {
    private PasswordValidator()
    {

    }

    public static boolean validatePassword(String password)
    {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$";
        Pattern p = Pattern.compile(regex);

        if(password==null)
        {
            return false;
        }

        return p.matcher(password).matches();

    }
}
