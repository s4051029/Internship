package com.mirrorchannelth.internship.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by boss on 5/22/16.
 */
public class DateUtil {

    public static Date getDate(String date, String formate){

        SimpleDateFormat dfm = new SimpleDateFormat(formate);
        Date dateObject = null;
        try {
            dateObject = dfm.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateObject;
    }
}
