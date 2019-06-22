package com.bigblackboy.doctorappointment.pojos.springpojos;

public class MyCommentsResponse {

    private int commentId;
    private String date;
    private String time;
    private String dateTime;
    private String text;
    private int reviewId;
    private int doctorId;
    private String doctorName;
    private String specialityName;
    private String lpuName;
    private String districtName;
    private int revAuthorId;
    private String revAuthorName;
    private String revAuthorLastname;
    private String revAuthorMiddlename;
    private int revAuthorDaybirth;
    private int revAuthorMonthbirth;
    private int revAuthorYearbirth;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRevAuthorId() {
        return revAuthorId;
    }

    public void setRevAuthorId(int revAuthorId) {
        this.revAuthorId = revAuthorId;
    }

    public String getRevAuthorName() {
        return revAuthorName;
    }

    public void setRevAuthorName(String revAuthorName) {
        this.revAuthorName = revAuthorName;
    }

    public String getRevAuthorLastname() {
        return revAuthorLastname;
    }

    public void setRevAuthorLastname(String revAuthorLastname) {
        this.revAuthorLastname = revAuthorLastname;
    }

    public String getRevAuthorMiddlename() {
        return revAuthorMiddlename;
    }

    public void setRevAuthorMiddlename(String revAuthorMiddlename) {
        this.revAuthorMiddlename = revAuthorMiddlename;
    }

    public int getRevAuthorDaybirth() {
        return revAuthorDaybirth;
    }

    public void setRevAuthorDaybirth(int revAuthorDaybirth) {
        this.revAuthorDaybirth = revAuthorDaybirth;
    }

    public int getRevAuthorMonthbirth() {
        return revAuthorMonthbirth;
    }

    public void setRevAuthorMonthbirth(int revAuthorMonthbirth) {
        this.revAuthorMonthbirth = revAuthorMonthbirth;
    }

    public int getRevAuthorYearbirth() {
        return revAuthorYearbirth;
    }

    public void setRevAuthorYearbirth(int revAuthorYearbirth) {
        this.revAuthorYearbirth = revAuthorYearbirth;
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

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public String getLpuName() {
        return lpuName;
    }

    public void setLpuName(String lpuName) {
        this.lpuName = lpuName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}
