package com.bigblackboy.doctorappointment.model;

import android.content.SharedPreferences;

import com.bigblackboy.doctorappointment.api.DoctorsApiResponse;
import com.bigblackboy.doctorappointment.api.ScheduleApiResponse;
import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.bigblackboy.doctorappointment.controller.HospitalController;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorModel {

    private HospitalApi hospitalApi;
    private List<Doctor> doctors;
    private SharedPreferences mSettings;
    private SharedPreferencesManager prefManager;

    public DoctorModel() {
        hospitalApi = HospitalController.getApi();
    }

    public DoctorModel(SharedPreferences prefs) {
        hospitalApi = HospitalController.getApi();
        mSettings = prefs;
        prefManager = new SharedPreferencesManager(mSettings);
    }

    public Doctor getChosenDoctor() {
        return prefManager.getCurrentDoctor();
    }

    public void setChosenDoctor(Doctor chosenDoctor) {
        prefManager.setCurrentDoctor(chosenDoctor);
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

    public void getSchedule(String doctorId, String hospitalId, String patientId, String historyId, String appointmentType, final OnGetScheduleListener listener) {
        hospitalApi.getSchedule(doctorId, hospitalId, "", "", "").enqueue(new Callback<ScheduleApiResponse>() {
            @Override
            public void onResponse(Call<ScheduleApiResponse> call, Response<ScheduleApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResponse().size() == 1) {
                        listener.onFinished(response.body().getResponse().get(0));
                    } else {
                        List<ScheduleApiResponse.MResponse> schedule = new ArrayList();
                        schedule.addAll(response.body().getResponse().get(0));
                        for (int i = 1; i < response.body().getResponse().size(); i++) {
                            schedule.addAll(response.body().getResponse().get(i));
                        }
                        listener.onFinished(schedule);
                    }
                } else listener.onFailure(new Throwable(response.body().getError().getErrorDescription()));
            }

            @Override
            public void onFailure(Call<ScheduleApiResponse> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public interface OnFinishedListener {
        void onFinished(List<Doctor> doctors);

        void onFailure(Throwable t);
    }

    public interface OnGetScheduleListener {
        void onFinished(List<ScheduleApiResponse.MResponse> schedule);

        void onFailure(Throwable t);
    }
}
