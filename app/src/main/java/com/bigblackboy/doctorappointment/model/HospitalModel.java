package com.bigblackboy.doctorappointment.model;

import com.bigblackboy.doctorappointment.api.HospitalApiResponse;
import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.bigblackboy.doctorappointment.controller.HospitalController;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalModel {

    private List<Hospital> hospitals;
    private HospitalApi hospitalApi;

    public HospitalModel() {
        hospitalApi = HospitalController.getApi();
    }

    public void getHospitals(String districtId, final OnFinishedListener onFinishedListener) {
        hospitalApi.getHospitals(districtId).enqueue(new Callback<HospitalApiResponse>() {
            @Override
            public void onResponse(Call<HospitalApiResponse> call, Response<HospitalApiResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    HospitalApiResponse res = response.body();
                    hospitals = res.getHospitals();
                    onFinishedListener.onFinished(hospitals);
                } else onFinishedListener.onFailure(new Throwable("Body is null. Code: " + response.code()));
            }

            @Override
            public void onFailure(Call<HospitalApiResponse> call, Throwable t) {
                onFinishedListener.onFailure(new Throwable("Error: " + t.getMessage()));
            }
        });
    }

    public interface OnFinishedListener {
        void onFinished(List<Hospital> hospitals);

        void onFailure(Throwable t);
    }

}
