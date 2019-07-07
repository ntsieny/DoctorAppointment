package com.bigblackboy.doctorappointment.presenter;

import android.text.TextUtils;

import com.bigblackboy.doctorappointment.model.UserModel;
import com.bigblackboy.doctorappointment.view.fragment.SignUpFragment;

import java.util.HashMap;

public class SignUpFragmentPresenter {

    private UserModel model;
    private SignUpFragment view;
    private boolean error;

    public void attachView(SignUpFragment view) {
        this.view = view;
        model = new UserModel();
    }

    public void viewIsReady() {

    }

    public void detachView() {
        view = null;
    }

    public void checkLoginUnique(String login, String serviceId) {
        if(login.length() > 6) {
            model.checkLoginUnique(login, serviceId, new UserModel.OnCheckLoginUnique() {
                @Override
                public void onFinished(boolean unique) {
                    if (unique) {
                        view.setInputError("");
                        error = false;
                    }
                    else {
                        view.setInputError("Логин занят");
                        error = true;
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    view.showToast(t.getMessage());
                    error = true;
                }
            });
        } else {
            view.setInputError("Логин должен быть длиннее 6 символов");
            error = true;
        }
    }

    public void onBtnSignupClick() {
        String login = view.getLoginInput();
        String password = view.getPasswordInput();
        String passwordRepeat = view.getPasswordRepeatInput();
        if(!(TextUtils.isEmpty(login)) && !(TextUtils.isEmpty(password) && !(TextUtils.isEmpty(passwordRepeat)))) {
            if(password.equals(passwordRepeat)) {
                if (!error) {
                    HashMap<String, String> hashMap = new HashMap();
                    hashMap.put("login", login);
                    hashMap.put("password", password);
                    view.sendDataToActivity(hashMap);
                } else view.showToast("Придумайте другой логин");
            } else view.showToast("Пароли не совпадают");
        } else view.showToast("Введите данные!");
    }
}
