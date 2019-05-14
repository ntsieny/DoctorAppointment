package com.bigblackboy.doctorappointment.utils;

import org.joda.time.LocalDate;
import org.joda.time.Years;

public class AgeCalculator {

    public static int getAge(LocalDate birthDate, LocalDate currentDate) {
        Years years = Years.yearsBetween(birthDate, currentDate);
        return years.getYears();
    }

    public static String getAgeVerbal(LocalDate birthDate, LocalDate currentDate) {
        Years years = Years.yearsBetween(birthDate, currentDate);
        int yrs = years.getYears();
        int lastDigit = yrs % 10;
        String appendString;
        if(lastDigit == 1)
            appendString = "год";
        else if(lastDigit == 2 || lastDigit == 3 || lastDigit == 4)
            appendString = "года";
        else appendString = "лет";
        return String.format("%d %s", yrs, appendString);
    }
}
