package com.bigblackboy.doctorappointment.model;

import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.pojos.springpojos.Appointment;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentModel {

    private SpringApi springApi;
    private List<Appointment> appointments;

    public AppointmentModel() {
        springApi = SpringController.getApi();
    }

    public void getAppointments(String serviceId, final OnFinishedListener onFinishedListener) {
        springApi.getAppointments(serviceId).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful()) {
                    if(response.body() != null && response.body().size() > 0) {
                        appointments = response.body();
                        onFinishedListener.onFinished(appointments);
                    } else onFinishedListener.onFailure(new Throwable("Записей не обнаружено"));
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        //onFinishedListener.onFailure(new Throwable("Ошибка сервера"));
                        onFinishedListener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        onFinishedListener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    public void deleteAppointment(int appointmentId, final com.bigblackboy.doctorappointment.model.OnFinishedListener onFinishedListener) {
        springApi.deleteAppointment(appointmentId).enqueue(new Callback<com.bigblackboy.doctorappointment.pojos.springpojos.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Response<com.bigblackboy.doctorappointment.pojos.springpojos.Response> response) {
                if (response.isSuccessful()) {
                    if(response.body() != null && response.body().isSuccess()) {
                        onFinishedListener.onFinished();
                    } else onFinishedListener.onFailure(new Throwable("Запись не отменена"));
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        onFinishedListener.onFailure(new Throwable(error.getString("message")));
                        //onFinishedListener.onFailure(new Throwable("Ошибка сервера"));
                    } catch (Exception e) {
                        onFinishedListener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    public interface OnFinishedListener {
        void onFinished(List<Appointment> appointments);

        void onFailure(Throwable t);

    }
}
