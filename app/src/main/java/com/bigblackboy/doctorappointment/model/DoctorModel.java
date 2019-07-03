package com.bigblackboy.doctorappointment.model;

import com.bigblackboy.doctorappointment.api.DoctorsApiResponse;
import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.bigblackboy.doctorappointment.controller.HospitalController;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorModel {

    private HospitalApi hospitalApi;
    private List<Doctor> doctors;

    public DoctorModel() {
        hospitalApi = HospitalController.getApi();
    }

    public void getDoctors(String specialityId, String hospitalId, String patientId, String historyId, final OnFinishedListener onFinishedListener) {
        hospitalApi.getDoctors(specialityId, hospitalId, patientId, "").enqueue(new Callback<DoctorsApiResponse>() {
            @Override
            public void onResponse(Call<DoctorsApiResponse> call, Response<DoctorsApiResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getSuccess()) {
                        DoctorsApiResponse respObj = response.body();
                        doctors = respObj.getDoctors();
                        onFinishedListener.onFinished(doctors);
                    } else onFinishedListener.onFailure(new Throwable("Ошибка: " + response.body().getError().getErrorDescription()));
                } else onFinishedListener.onFailure(new Throwable("Запрос не прошел (" + response.code() + ")"));
            }

            @Override
            public void onFailure(Call<DoctorsApiResponse> call, Throwable t) {
                onFinishedListener.onFailure(new Throwable("Error: " + t.getMessage()));
            }
        });
    }

    public interface OnFinishedListener {
        void onFinished(List<Doctor> doctors);

        void onFailure(Throwable t);
    }
}
