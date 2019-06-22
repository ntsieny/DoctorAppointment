package com.bigblackboy.doctorappointment.pojos.springpojos;

public class User {

    private String name;
    private String lastname;
    private String middlename;
    private int dayBirth;
    private int monthBirth;
    private int yearBirth;
    private String serviceId;
    private String login;
    private String password;
    private int patientId;
    private int districtId;
    private String districtName;
    private int hospitalId;
    private String lpuNameShort;
    private String lpuNameFull;
    private String lpuAddress;
    private String lpuEmail;
    private String lpuType;
    private String lpuWorkTime;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getLpuNameShort() {
        return lpuNameShort;
    }

    public void setLpuNameShort(String lpuNameShort) {
        this.lpuNameShort = lpuNameShort;
    }

    public String getLpuNameFull() {
        return lpuNameFull;
    }

    public void setLpuNameFull(String lpuNameFull) {
        this.lpuNameFull = lpuNameFull;
    }

    public String getLpuAddress() {
        return lpuAddress;
    }

    public void setLpuAddress(String lpuAddress) {
        this.lpuAddress = lpuAddress;
    }

    public String getLpuEmail() {
        return lpuEmail;
    }

    public void setLpuEmail(String lpuEmail) {
        this.lpuEmail = lpuEmail;
    }

    public String getLpuType() {
        return lpuType;
    }

    public void setLpuType(String lpuType) {
        this.lpuType = lpuType;
    }

    public String getLpuWorkTime() {
        return lpuWorkTime;
    }

    public void setLpuWorkTime(String lpuWorkTime) {
        this.lpuWorkTime = lpuWorkTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
