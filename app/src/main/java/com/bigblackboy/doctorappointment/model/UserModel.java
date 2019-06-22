package com.bigblackboy.doctorappointment.model;

import android.content.SharedPreferences;

import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_USER_LOGGED_IN;

public class UserModel {

    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;

    public UserModel(SharedPreferences mSettings) {
        this.mSettings = mSettings;
    }

    public void editSharedPrefsUserLoggedIn(boolean value) {
        editor = mSettings.edit();
        editor.putBoolean(APP_SETTINGS_USER_LOGGED_IN, value).apply();
    }
}
