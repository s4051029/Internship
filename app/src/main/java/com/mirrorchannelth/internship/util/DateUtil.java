package com.mirrorchannelth.internship.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by boss on 5/22/16.
 */
public class DateUtil {

    public static String getCurrentDate(String dfm){

        DateFormat dateFormat = new SimpleDateFormat(dfm);
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        return currentDate;
    }
    public static String changeFormatDate(String oldFormat, String newFormat, String date){
        String newDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
        try {
            Date d = sdf.parse(date);
            sdf.applyPattern(newFormat);
            newDate = sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

}
