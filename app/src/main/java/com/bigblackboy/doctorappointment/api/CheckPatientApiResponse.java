package com.bigblackboy.doctorappointment.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckPatientApiResponse extends ApiResponse {

    @SerializedName("response")
    @Expose
    private Response response;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private Error error;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
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



    @Override
    public String toString() {
        return "HistoryId: " + response.getHistoryId() + "\nPatientAriaNumber: " + response.getPatientAriaNumber() + "\nPatientId: " + response.getPatientId();
    }

    public class Response {

        @SerializedName("history_id")
        @Expose
        private String historyId;
        @SerializedName("patientAriaNumber")
        @Expose
        private String patientAriaNumber;
        @SerializedName("patient_id")
        @Expose
        private String patientId;

        public String getHistoryId() {
            return historyId;
        }

        public void setHistoryId(String historyId) {
            this.historyId = historyId;
        }

        public String getPatientAriaNumber() {
            return patientAriaNumber;
        }

        public void setPatientAriaNumber(String patientAriaNumber) {
            this.patientAriaNumber = patientAriaNumber;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }
    }
}
