package com.bigblackboy.doctorappointment.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.UserModel;
import com.bigblackboy.doctorappointment.presenter.ChooseLoginActivityPresenter;

import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_GUEST_MODE;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_USER_LOGGED_IN;

public class ChooseLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ChooseLoginActivityPresenter presenter;
    private Button btnLoginUser, btnLoginGuest, btnRegistration;
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void init() {
        mSettings = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE);
        boolean loggedId = mSettings.getBoolean(APP_SETTINGS_USER_LOGGED_IN, false);
        boolean guestMode = mSettings.getBoolean(APP_SETTINGS_GUEST_MODE, false);
        if (loggedId | guestMode) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else {
            setContentView(R.layout.activity_choose_login);
            btnLoginUser = findViewById(R.id.btnLoginUser);
            btnLoginUser.setOnClickListener(this);
            btnLoginGuest = findViewById(R.id.btnLoginGuest);
            btnLoginGuest.setOnClickListener(this);
            btnRegistration = findViewById(R.id.btnRegistration);
            btnRegistration.setOnClickListener(this);
        }
        UserModel userModel = new UserModel(mSettings);
        presenter = new ChooseLoginActivityPresenter(userModel);
        presenter.attachView(this);
    }

    public void showLoginActivity() {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void showRegistrationActivity() {
        finish();
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    public void showMainActivity() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginUser:
                presenter.loginAsUser();
                break;
            case R.id.btnRegistration:
                presenter.register();
                break;
            case R.id.btnLoginGuest:
                presenter.loginAsGuest();
                break;
        }
    }
}
