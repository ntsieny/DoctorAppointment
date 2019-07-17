package com.bigblackboy.doctorappointment.model;

import android.content.SharedPreferences;

import com.bigblackboy.doctorappointment.api.AppointmentListApiResponse;
import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.bigblackboy.doctorappointment.controller.HospitalController;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.AppointmentInfo;
import com.bigblackboy.doctorappointment.pojos.springpojos.Appointment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentModel {

    private SpringApi springApi;
    private HospitalApi hospitalApi;
    private SharedPreferences mSettings;
    private SharedPreferencesManager prefManager;

    public AppointmentModel() {
        springApi = SpringController.getApi();
        hospitalApi = HospitalController.getApi();
    }

    public AppointmentModel(SharedPreferences prefs) {
        this.mSettings = prefs;
        prefManager = new SharedPreferencesManager(mSettings);
        springApi = SpringController.getApi();
        hospitalApi = HospitalController.getApi();
    }

    public void getAppointments(String serviceId, final OnFinishedListener onFinishedListener) {
        springApi.getAppointments(serviceId).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful()) {
                    if(response.body() != null && response.body().size() > 0) {
                        List<Appointment> appointments = response.body();
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

    public void getAppointmentDates(String doctorId, String hospitalId, String patientId, String historyId, String appointmentType, final AppointmentDatesListener listener) {
        hospitalApi.getAppointments(doctorId, hospitalId, patientId, "", "").enqueue(new Callback<AppointmentListApiResponse>() {
            @Override
            public void onResponse(Call<AppointmentListApiResponse> call, Response<AppointmentListApiResponse> response) {
                if (response.isSuccessful()) {
                    AppointmentListApiResponse respObj = response.body();
                    List<AppointmentInfo> infoList;
                    List<AppointmentInfo> appoints = new ArrayList<>();
                    Map<String, List<AppointmentInfo>> resp = respObj.getResponse();
                    if(respObj.getSuccess()) {
                        for (Map.Entry entry : resp.entrySet()) {
                            //Log.d(LOG_TAG, "Дата: " + entry.getKey() + "\n");
                            infoList = (List<AppointmentInfo>) entry.getValue();
                            for (AppointmentInfo info : infoList) {
                                appoints.add(info);
                            }
                        }
                        listener.onFinished(appoints);
                    } else listener.onFailure(new Throwable("Ошибка " + respObj.getError().getIdError() + ". " + respObj.getError().getErrorDescription()));
                } else listener.onFailure(new Throwable("Запрос не прошел (" + response.code() + ")"));
            }

            @Override
            public void onFailure(Call<AppointmentListApiResponse> call, Throwable t) {
                listener.onFailure(t);
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

    public void createAppointment(Appointment app, final com.bigblackboy.doctorappointment.model.OnFinishedListener listener) {
        springApi.createAppointment(app).enqueue(new Callback<com.bigblackboy.doctorappointment.pojos.springpojos.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, retrofit2.Response<com.bigblackboy.doctorappointment.pojos.springpojos.Response> response) {
                com.bigblackboy.doctorappointment.pojos.springpojos.Response resp = response.body();
                if (response.isSuccessful()) {
                    if (resp != null && resp.isSuccess()) listener.onFinished();
                    else listener.onFailure(new Throwable("Запись не сделана"));
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        listener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        listener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public interface OnFinishedListener {
        void onFinished(List<Appointment> appointments);

        void onFailure(Throwable t);
    }

    // TODO как обобщить интерфейсы
    public interface AppointmentDatesListener {
        void onFinished(List<AppointmentInfo> appointmentDates);

        void onFailure(Throwable t);
    }
}
