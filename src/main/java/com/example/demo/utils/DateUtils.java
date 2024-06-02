package com.example.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    public static String formatTimestamp(Long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
        String formattedTime = dateFormat.format(new Date(timestamp));
        return formattedTime;
    }

}
