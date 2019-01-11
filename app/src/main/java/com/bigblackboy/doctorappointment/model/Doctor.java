package com.bigblackboy.doctorappointment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Doctor {

    @SerializedName("AriaNumber")
    @Expose
    private String ariaNumber;

    @SerializedName("Comment")
    @Expose
    private String comment;

    @SerializedName("CountFreeParticipantIE")
    @Expose
    private String countFreeParticipants;

    @SerializedName("CountFreeTicket")
    @Expose
    private String countFreeTickets;

    @SerializedName("IdDoc")
    @Expose
    private String idDoc;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("Snils")
    @Expose
    private String snils;

    @SerializedName("LastDate")
    @Expose
    private Date lastDate;

    @SerializedName("NearestDate")
    @Expose
    private Date nearestDate;

    public String getCountFreeParticipants() {
        return countFreeParticipants;
    }

    public void setCountFreeParticipants(String countFreeParticipants) {
        this.countFreeParticipants = countFreeParticipants;
    }

    public String getCountFreeTickets() {
        return countFreeTickets;
    }

    public void setCountFreeTickets(String countFreeTickets) {
        this.countFreeTickets = countFreeTickets;
    }

    public String getAriaNumber() {
        return ariaNumber;
    }

    public void setAriaNumber(String ariaNumber) {
        this.ariaNumber = ariaNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(String idDoc) {
        this.idDoc = idDoc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public Date getNearestDate() {
        return nearestDate;
    }

    public void setNearestDate(Date nearestDate) {
        this.nearestDate = nearestDate;
    }


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
    }
}
