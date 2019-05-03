package com.bigblackboy.doctorappointment.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateParser {

    public static String convertISOtoDateTimeString(String dateIso) {
        DateTimeFormatter parser = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime = parser.parseDateTime(dateIso);
        DateTimeFormatter converter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm");
        return converter.print(dateTime);
    }

    public static String convertISOwithMillistoDateTimeString(String dateIso) {
        DateTimeFormatter parser = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        DateTime dateTime = parser.parseDateTime(dateIso);
        DateTimeFormatter converter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm");
        return converter.print(dateTime);
    }
}
