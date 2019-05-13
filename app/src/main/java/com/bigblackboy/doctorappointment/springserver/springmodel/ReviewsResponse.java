package com.bigblackboy.doctorappointment.springserver.springmodel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ReviewsResponse extends Review implements Serializable {

    //reviewId, date, time, dateTime, text, mark, doctorId, docName, serviceId, authorId, specId, specName, lpuId, lpuName, districtId, districtName унаследовано
    private String name;
    private String lastname;
    private String middlename;
    private int daybirth;
    private int monthbirth;
    private int yearbirth;
    private int likes;
    private int dislikes;
    private List likers;
    private List dislikers;
    private int commentCount;

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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
