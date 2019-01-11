package com.bigblackboy.doctorappointment.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error {

    @SerializedName("ErrorDescription")
    @Expose
    private String errorDescription;
    @SerializedName("IdError")
    @Expose
    private Integer idError;

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Integer getIdError() {
        return idError;
    }

    public void setIdError(Integer idError) {
        this.idError = idError;
    }
}
