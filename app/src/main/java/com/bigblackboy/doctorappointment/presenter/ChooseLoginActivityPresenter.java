package com.bigblackboy.doctorappointment.presenter;

import android.content.SharedPreferences;

import com.bigblackboy.doctorappointment.model.UserModel;
import com.bigblackboy.doctorappointment.view.activity.ChooseLoginActivity;

public class ChooseLoginActivityPresenter {

    private ChooseLoginActivity view;
    private UserModel model;

    public ChooseLoginActivityPresenter(SharedPreferences prefs) {
        model = new UserModel(prefs);
    }

    public void attachView(ChooseLoginActivity activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void loginAsUser() {
        view.showLoginActivity();
    }

    public void register() {
        view.showRegistrationActivity();
    }

    public void loginAsGuest() {
        model.setUserLoggedIn(false);
        view.showMainActivity();
    }

    public void onCreateActivity() {
        if (model.isUserLoggedIn() | model.isGuestMode()) {
            view.showMainActivity();
        } else view.initLayout();
    }
}
