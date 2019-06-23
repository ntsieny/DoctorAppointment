package com.bigblackboy.doctorappointment.presenter;

import android.text.TextUtils;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.UserModel;
import com.bigblackboy.doctorappointment.pojos.springpojos.User;
import com.bigblackboy.doctorappointment.view.activity.LoginActivity;

public class LoginActivityPresenter {

    private LoginActivity view;
    private UserModel model;

    public LoginActivityPresenter(UserModel model) {
        this.model = model;
    }

    public void attachView(LoginActivity activity) {
        view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void onBtnLoginClick(String login, String password) {
        if(!TextUtils.isEmpty(login) && !TextUtils.isEmpty(password)) {
            model.getUserByLoginPassword(login, password, new UserModel.OnFinishedListener() {
                @Override
                public void onFinished(User user) {
                    view.openMainActivity();
                    view.showToast(String.format("%s, %s %s!", view.getResources().getString(R.string.welcome), user.getName(), user.getMiddlename()));
                }

                @Override
                public void onFailure(Throwable t) {
                    view.showToast(t.getMessage());
                }
            });
        } else view.showToast(R.string.toast_you_have_to_input_data);
    }
}
