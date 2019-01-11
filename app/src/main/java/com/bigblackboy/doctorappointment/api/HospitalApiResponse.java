package com.bigblackboy.doctorappointment.api;

import com.bigblackboy.doctorappointment.api.ApiResponse;
import com.bigblackboy.doctorappointment.model.Hospital;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HospitalApiResponse extends ApiResponse {

    @SerializedName("response")
    @Expose
    private List<Hospital> hospitals;

    public List<Hospital> getHospitals() {
        return hospitals;
    }

    public void setHospitals(List<Hospital> hospitals) {
        this.hospitals = hospitals;
    }
}
