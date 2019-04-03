package com.bigblackboy.doctorappointment.springserver.springmodel;

public class User {

    private Patient patient;
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
