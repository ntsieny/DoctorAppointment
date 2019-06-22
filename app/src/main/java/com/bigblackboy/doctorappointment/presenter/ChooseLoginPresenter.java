package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.model.UserModel;
import com.bigblackboy.doctorappointment.view.activity.ChooseLoginActivity;

public class ChooseLoginPresenter {

    private ChooseLoginActivity view;
    private UserModel model;

    public ChooseLoginPresenter(UserModel model) {
        this.model = model;
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
        model.editSharedPrefsUserLoggedIn(false);
        view.showMainActivity();
    }
}
