package com.bigblackboy.doctorappointment.model;

import com.bigblackboy.doctorappointment.api.SpecialitiesApiResponse;
import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.bigblackboy.doctorappointment.controller.HospitalController;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecialityModel {

    private List<Speciality> specialities;
    private HospitalApi hospitalApi;

    public SpecialityModel() {
        hospitalApi = HospitalController.getApi();
    }

    public void getSpecialities(String hospitalId, String historyId, String patientAriaNumber, String patientId, final OnFinishedListener onFinishedListener) {
        hospitalApi.getSpecialities(hospitalId, "", "", patientId).enqueue(new Callback<SpecialitiesApiResponse>() {
            @Override
            public void onResponse(Call<SpecialitiesApiResponse> call, Response<SpecialitiesApiResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getSuccess()) {
                        SpecialitiesApiResponse respObj = response.body();
                        specialities = respObj.getSpecialities();
                        onFinishedListener.onFinished(specialities);
                    } else onFinishedListener.onFailure(new Throwable("Ошибка: " + response.body().getError().getErrorDescription()));
                } else onFinishedListener.onFailure(new Throwable("Запрос не прошел (" + response.code() + ")"));
            }

            @Override
            public void onFailure(Call<SpecialitiesApiResponse> call, Throwable t) {
                onFinishedListener.onFailure(new Throwable("Error: " + t.getMessage()));
            }
        });
    }

    public interface OnFinishedListener {
        void onFinished(List<Speciality> specialities);

        void onFailure(Throwable t);
    }
}
