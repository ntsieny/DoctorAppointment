package com.bigblackboy.doctorappointment.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateAppointmentApiResponse extends ApiResponse {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private Error error;

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
}
