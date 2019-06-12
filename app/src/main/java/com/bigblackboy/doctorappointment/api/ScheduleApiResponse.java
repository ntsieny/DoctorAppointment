package com.bigblackboy.doctorappointment.api;

import com.bigblackboy.doctorappointment.model.AppointmentInfo;
import com.bigblackboy.doctorappointment.model.Date;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ScheduleApiResponse extends ApiResponse {

    @SerializedName("response")
    @Expose
    private List<List<MResponse>> response = null;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private Error error;

    public List<List<MResponse>> getResponse() {
        return response;
    }

    public void setResponse(List<List<MResponse>> response) {
        this.response = response;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public class MResponse {

        @SerializedName("denyCause")
        @Expose
        private String denyCause;
        @SerializedName("date_end")
        @Expose
        private Date dateEnd;
        @SerializedName("date_start")
        @Expose
        private Date dateStart;
        @SerializedName("deny")
        @Expose
        private boolean deny;

        public String getDenyCause() {
            return denyCause;
        }

        public void setDenyCause(String denyCause) {
            this.denyCause = denyCause;
        }

        public Date getDateEnd() {
            return dateEnd;
        }

        public void setDateEnd(Date dateEnd) {
            this.dateEnd = dateEnd;
        }

        public Date getDateStart() {
            return dateStart;
        }

        public void setDateStart(Date dateStart) {
            this.dateStart = dateStart;
        }

        public boolean isDeny() {
            return deny;
        }

        public void setDeny(boolean deny) {
            this.deny = deny;
        }

        @Override
        public String toString() {
            return String.format("%s %s - %s-%s", dateStart.getDay(), dateStart.getMonthVerbose(), dateStart.getTime(), dateEnd.getTime()) ;
        }
    }
}
