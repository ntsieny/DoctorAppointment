package com.bigblackboy.doctorappointment.pojos.hospitalpojos;

import com.google.gson.annotations.SerializedName;

public class AppointmentInfo implements Comparable<AppointmentInfo> {
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
        return dateStart.getDay() + " " + dateStart.getMonthVerbose() + " " + dateStart.getTime() + ", " + dateStart.getDayVerbose();
    }

    @Override
    public int compareTo(AppointmentInfo o) {
        return this.id.compareTo(o.getId());
    }
}




