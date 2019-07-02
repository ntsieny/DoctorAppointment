package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.model.UserModel;
import com.bigblackboy.doctorappointment.pojos.springpojos.User;
import com.bigblackboy.doctorappointment.view.activity.RegistrationActivity;

public class RegistrationActivityPresenter {

    private RegistrationActivity view;
    private UserModel model;

    public RegistrationActivityPresenter(UserModel model) {
        this.model = model;
    }

    public void attachView(RegistrationActivity view) {
        this.view = view;
    }

    public void detachView() {
        view = null;
    }

    public void createUser() {
        model.createUser(view.createUserObjectForRequest(), new UserModel.OnFinishedListener() {
            @Override
            public void onFinished(User user) {
                view.openMainMenuActivity();
                view.showToast(String.format("Добро пожаловать, %s!", user.getName()));
            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }
}
