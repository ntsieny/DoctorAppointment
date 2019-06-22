package com.bigblackboy.doctorappointment.pojos.hospitalpojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Speciality implements Serializable {

    public Speciality() {

    }

    public Speciality(String specialityId, String specialityName) {
        this.idSpeciality = specialityId;
        this.nameSpeciality = specialityName;
    }

    @SerializedName("CountFreeParticipantIE")
    @Expose
    private String countFreeParticipants;

    @SerializedName("CountFreeTicket")
    @Expose
    private int countFreeTickets;

    @SerializedName("FerIdSpesiality")
    @Expose
    private String ferIdSpeciality;

    @SerializedName("IdSpesiality")
    @Expose
    private String idSpeciality;

    @SerializedName("LastDate")
    @Expose
    private Date lastDate;

    @SerializedName("NameSpesiality")
    @Expose
    private String nameSpeciality;

    @SerializedName("NearestDate")
    @Expose
    private Date nearestDate;

    public String getCountFreeParticipants() {
        return countFreeParticipants;
    }

    public void setCountFreeParticipants(String countFreeParticipants) {
        this.countFreeParticipants = countFreeParticipants;
    }

    public int getCountFreeTickets() {
        return countFreeTickets;
    }

    public void setCountFreeTickets(int countFreeTickets) {
        this.countFreeTickets = countFreeTickets;
    }

    public String getFerIdSpeciality() {
        return ferIdSpeciality;
    }

    public void setFerIdSpeciality(String ferIdSpeciality) {
        this.ferIdSpeciality = ferIdSpeciality;
    }

    public String getIdSpeciality() {
        return idSpeciality;
    }

    public void setIdSpeciality(String idSpeciality) {
        this.idSpeciality = idSpeciality;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public String getNameSpeciality() {
        return nameSpeciality;
    }

    public void setNameSpeciality(String nameSpeciality) {
        this.nameSpeciality = nameSpeciality;
    }

    public Date getNearestDate() {
        return nearestDate;
    }

    public void setNearestDate(Date nearestDate) {
        this.nearestDate = nearestDate;
    }

    @Override
    public String toString() {
        if(getCountFreeTickets() > 0)
            return nameSpeciality + " (" + getCountFreeTickets() + " талонов)";
        else return nameSpeciality;
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
