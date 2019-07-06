package com.bigblackboy.doctorappointment.pojos.springpojos;

import java.util.List;

public class CommentResponse implements Comparable<CommentResponse> {

    private int commentId;
    private int reviewId;
    private String date;
    private String time;
    private String dateTime;
    private String text;
    private int authorId;
    private String name;
    private String lastname;
    private String middlename;
    private int daybirth;
    private int monthbirth;
    private int yearbirth;
    private String serviceId;
    private int likes;
    private int dislikes;
    private List likers;
    private List dislikers;

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

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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
    public int compareTo(CommentResponse o) {
        return dateTime.compareTo(o.dateTime);
    }
}
