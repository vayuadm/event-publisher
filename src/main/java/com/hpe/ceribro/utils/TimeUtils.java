package com.hpe.ceribro.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    private static final long ONE_MINUTE_IN_MILLIS=60000;
    private static final long ONE_SECOND_IN_MILLIS=1000;

    public static String getCurrentTimePlusMinutesFormatted(int minutes, String format) {

        return formatDate(getCurrentTimePlusMinutes(minutes), format);
    }

    public static String getCurrentTimePlusSecondsFormatted(int second, String format) {

        return formatDate(getCurrentTimePlusSeconds(second), format);
    }

    private static String formatDate(Date date, String format) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    private static Date getCurrentTimePlusMinutes(int minutes) {

        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();

        return new Date(t + (minutes * ONE_MINUTE_IN_MILLIS));

    }

    private static Date getCurrentTimePlusSeconds(int seconds) {

        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();

        return new Date(t + (seconds * ONE_SECOND_IN_MILLIS));

    }
}
