package com.bigblackboy.doctorappointment.pojos.hospitalpojos;

public class Patient {

    private String serviceId;
    private String name;
    private String lastName;
    private String middleName;
    private String insuranceSeries;
    private String insuranceNumber;
    private int dayBirth;
    private int monthBirth;
    private int yearBirth;
    private District district;
    private Hospital hospital;

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
        return String.format("%d-%d-%dT00:00:00.000Z", yearBirth, monthBirth, dayBirth);
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", name, lastName, getBirthdayFormatted());
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}
