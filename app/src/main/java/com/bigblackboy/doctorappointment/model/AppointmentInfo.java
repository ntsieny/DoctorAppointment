package com.bigblackboy.doctorappointment.model;

import com.google.gson.annotations.SerializedName;

public class AppointmentInfo {
    @SerializedName("date_start")
    private Date dateStart;

    @SerializedName("date_end")
    private Date dateEnd;

    @SerializedName("id")
    private String id;

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DateStart:\n" + dateStart.toString() + "\n" + "DateEnd:\n" + dateEnd.toString() + "\nID: " + getId() + "\n";
    }
}




