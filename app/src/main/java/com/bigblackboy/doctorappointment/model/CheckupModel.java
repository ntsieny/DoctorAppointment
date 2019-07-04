package com.bigblackboy.doctorappointment.model;

import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.pojos.springpojos.HealthInfo;

import org.joda.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckupModel {

    private SpringApi springApi;

    public CheckupModel() {
        springApi = SpringController.getApi();
    }

    public void getHealthInfo(int age, final OnFinishedListener onFinishedListener) {
        springApi.getHealthInfo(age).enqueue(new Callback<HealthInfo>() {
            @Override
            public void onResponse(Call<HealthInfo> call, Response<HealthInfo> response) {
                if (response.body() != null) {
                    HealthInfo info = response.body();
                    onFinishedListener.onFinished(info);
                } else onFinishedListener.onFailure(new Throwable("Ошибка получения данных"));
            }

            @Override
            public void onFailure(Call<HealthInfo> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    public int getCheckupYear(int age) {
        if ((age >= 40) | (age < 40 && age % 3 == 0)) {
            return new LocalDate().getYear();
        } else {
            int nextAge = age;
            do { nextAge++; }
            while (nextAge % 3 != 0);
            return new LocalDate().getYear() + (nextAge - age);
        }
    }

    public interface OnFinishedListener {
        void onFinished(HealthInfo info);

        void onFailure(Throwable t);
    }
}
