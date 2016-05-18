package com.mirrorchannelth.internship.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidation {

    public static boolean isEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPassword(String password) {
        int minLength = 4;
        if (password != null && password.length() >= minLength) {
            return true;
        }
        return false;
    }

    public static boolean isPhoneNumber(String phoneNumber) {
        String phoneNumberPattern = "^[0-9]{10}$";
        if (phoneNumber.matches(phoneNumberPattern)) {
            return true;
        }
        return false;
    }

}
