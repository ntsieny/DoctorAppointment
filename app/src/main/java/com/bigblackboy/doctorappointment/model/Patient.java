package com.bigblackboy.doctorappointment.model;

public class Patient {

    private String id;
    private String name;
    private String lastName;
    private String middleName;
    private String insuranceSeries;
    private String insuranceNumber;
    private int dayBirth;
    private int monthBirth;
    private int yearBirth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getInsuranceSeries() {
        return insuranceSeries;
    }

    public void setInsuranceSeries(String insuranceSeries) {
        this.insuranceSeries = insuranceSeries;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public int getDayBirth() {
        return dayBirth;
    }

    public void setDayBirth(int dayBirth) {
        this.dayBirth = dayBirth;
    }

    public int getMonthBirth() {
        return monthBirth;
    }

    public void setMonthBirth(int monthBirth) {
        this.monthBirth = monthBirth;
    }

    public int getYearBirth() {
        return yearBirth;
    }

    public void setYearBirth(int yearBirth) {
        this.yearBirth = yearBirth;
    }

    public String getBirthdayFormatted() {
        return String.format("%d-%d-%dT00:00:00.000Z", yearBirth, monthBirth + 1, dayBirth);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
