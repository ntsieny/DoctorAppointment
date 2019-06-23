package com.bigblackboy.doctorappointment.model;

import android.content.SharedPreferences;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.pojos.springpojos.User;
import com.bigblackboy.doctorappointment.utils.MyApplication;

import retrofit2.Call;
import retrofit2.Callback;

import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_DISTRICT_ID;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_DISTRICT_NAME;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_ID;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_NAME_FULL;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_NAME_SHORT;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_DAYBIRTH;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_ID;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_LASTNAME;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_MIDDLENAME;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_MONTHBIRTH;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_NAME;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_YEARBIRTH;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_USER_LOGGED_IN;

public class UserModel {

    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;
    private SpringApi springApi;

    public UserModel(SharedPreferences mSettings) {
        this.mSettings = mSettings;
        springApi = SpringController.getApi();
    }

    public void editSharedPrefsUserLoggedIn(boolean value) {
        editor = mSettings.edit();
        editor.putBoolean(APP_SETTINGS_USER_LOGGED_IN, value).apply();
    }

    private void writeSharedPreferences(User user) {
        editor = mSettings.edit();
        editor.putString(APP_SETTINGS_PATIENT_ID, user.getServiceId());
        editor.putString(APP_SETTINGS_PATIENT_NAME, user.getName());
        editor.putString(APP_SETTINGS_PATIENT_LASTNAME, user.getLastname());
        editor.putString(APP_SETTINGS_PATIENT_MIDDLENAME, user.getMiddlename());
        editor.putInt(APP_SETTINGS_PATIENT_DAYBIRTH, user.getDayBirth());
        editor.putInt(APP_SETTINGS_PATIENT_MONTHBIRTH, user.getMonthBirth());
        editor.putInt(APP_SETTINGS_PATIENT_YEARBIRTH, user.getYearBirth());
        editor.putString(APP_SETTINGS_DISTRICT_ID, String.valueOf(user.getDistrictId()));
        editor.putString(APP_SETTINGS_DISTRICT_NAME, user.getDistrictName());
        editor.putInt(APP_SETTINGS_HOSPITAL_ID, user.getHospitalId());
        editor.putString(APP_SETTINGS_HOSPITAL_NAME_SHORT, user.getLpuNameShort());
        editor.putString(APP_SETTINGS_HOSPITAL_NAME_FULL, user.getLpuNameFull());
        editor.apply();
    }

    public void getUserByLoginPassword(String login, String password, final OnFinishedListener onFinishedListener) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        springApi.getUserByLoginPassword(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                User user = response.body();
                if (response.isSuccessful() && user != null) {
                    writeSharedPreferences(user);
                    editSharedPrefsUserLoggedIn(true);
                    onFinishedListener.onFinished(user);
                } else onFinishedListener.onFailure(new Throwable(MyApplication.getAppContext().getResources().getString(R.string.user_not_found)));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    public interface OnFinishedListener {
        void onFinished(User user);

        void onFailure(Throwable t);
    }
}
