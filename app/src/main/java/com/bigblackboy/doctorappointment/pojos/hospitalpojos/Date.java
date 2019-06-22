package com.bigblackboy.doctorappointment.pojos.hospitalpojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Date {
    @SerializedName("day")
    @Expose
    private String day;

    @SerializedName("day_verbose")
    @Expose
    private String dayVerbose;

    @SerializedName("iso")
    @Expose
    private String dateTime;

    @SerializedName("month")
    @Expose
    private String month;

    @SerializedName("month_verbose")
    @Expose
    private String monthVerbose;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("year")
    @Expose
    private String year;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDayVerbose() {
        return dayVerbose;
    }

    public void setDayVerbose(String dayVerbose) {
        this.dayVerbose = dayVerbose;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthVerbose() {
        return monthVerbose;
    }

    public void setMonthVerbose(String monthVerbose) {
        this.monthVerbose = monthVerbose;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("Day: %s\nDayVerbose: %s\nISO: %s\nMonth: %s\nMonthVerbose: %s\nTime: %s\nYear: %s",
                getDay(), getDayVerbose(), getDateTime(), getMonth(), getMonthVerbose(), getTime(), getYear());
    }
}
