package com.mirrorchannelth.internship.util;

import android.content.Context;
import android.widget.EditText;

import com.mirrorchannelth.internship.R;

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

    private static boolean isEmpty(String text){
        if("".equals(text) || null == text)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(EditText editText, Context context) {
        if(FormValidation.isEmpty(editText.getText().toString())){
            editText.setError(context.getString(R.string.empty_text_error));
            editText.requestFocus();
            return true;
        } else {
            editText.setError(null);
        }
        return false;
    }

}
