package com.bigblackboy.doctorappointment.pojos.springpojos;

import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
