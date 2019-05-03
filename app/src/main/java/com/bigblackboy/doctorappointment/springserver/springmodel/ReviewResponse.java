package com.bigblackboy.doctorappointment.springserver.springmodel;

import java.util.Date;
import java.util.List;

public class ReviewResponse {

    private int reviewId;
    private Date date;
    private String time;
    private String dateTime;
    private String text;
    private String name;
    private String lastname;
    private String middlename;
    private int daybirth;
    private int monthbirth;
    private int yearbirth;
    private String serviceId;
    private int authorId;
    private int mark;
    private int likes;
    private int dislikes;
    private List likers;
    private List dislikers;
    private int commentCount;

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public int getDaybirth() {
        return daybirth;
    }

    public void setDaybirth(int daybirth) {
        this.daybirth = daybirth;
    }

    public int getMonthbirth() {
        return monthbirth;
    }

    public void setMonthbirth(int monthbirth) {
        this.monthbirth = monthbirth;
    }

    public int getYearbirth() {
        return yearbirth;
    }

    public void setYearbirth(int yearbirth) {
        this.yearbirth = yearbirth;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public List getLikers() {
        return likers;
    }

    public void setLikers(List likers) {
        this.likers = likers;
    }

    public List getDislikers() {
        return dislikers;
    }

    public void setDislikers(List dislikers) {
        this.dislikers = dislikers;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
