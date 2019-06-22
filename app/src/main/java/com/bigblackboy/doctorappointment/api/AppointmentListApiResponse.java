package com.bigblackboy.doctorappointment.api;

import com.bigblackboy.doctorappointment.model.hospitalmodel.AppointmentInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class AppointmentListApiResponse extends ApiResponse {

    @SerializedName("response")
    private Map<String, List<AppointmentInfo>> response;
    @SerializedName("success")
    private Boolean success;
    @SerializedName("error")
    private Error error;

    public Boolean getSuccess() { return success; }

    public void setSuccess(Boolean success) { this.success = success; }

    public Error getError() { return error; }

    public void setError(Error error) { this.error = error; }

    public Map<String, List<AppointmentInfo>> getResponse() {
        return response;
    }

    public void setResponse(Map<String, List<AppointmentInfo>> response) {
        this.response = response;
    }
}
