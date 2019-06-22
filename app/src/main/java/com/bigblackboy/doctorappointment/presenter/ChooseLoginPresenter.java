package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.view.activity.ChooseLoginActivity;

public class ChooseLoginPresenter {

    private ChooseLoginActivity view;

    public void attachView(ChooseLoginActivity activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void loginAsUser() {

    }

    public void register() {

    }

    public void loginAsGuest() {

    }
}
