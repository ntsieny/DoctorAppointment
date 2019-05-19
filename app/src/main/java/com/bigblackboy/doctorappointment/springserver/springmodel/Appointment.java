package com.bigblackboy.doctorappointment.springserver.springmodel;

import java.util.Date;

public class Appointment implements Comparable<Appointment> {

    private int appId;
    private String appString;
    private int patientId;
    private String serviceId;
    private int districtId;
    private String districtName;
    private int lpuId;
    private String lpuNameShort;
    private String lpuNameFull;
    private int specId;
    private String specName;
    private int docId;
    private String docName;
    private String date;
    private String time;
    private String dateTime;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppString() {
        return appString;
    }

    public void setAppString(String appString) {
        this.appString = appString;
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

    public int getLpuId() {
        return lpuId;
    }

    public void setLpuId(int lpuId) {
        this.lpuId = lpuId;
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

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int compareTo(Appointment o) {
        return appString.compareTo(o.appString);
    }
}
