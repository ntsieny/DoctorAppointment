package com.bigblackboy.doctorappointment.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.presenter.ChooseLoginActivityPresenter;

import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS;

public class ChooseLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ChooseLoginActivityPresenter presenter;
    private Button btnLoginUser, btnLoginGuest, btnRegistration;
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettings = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE);
        presenter = new ChooseLoginActivityPresenter(mSettings);
        presenter.attachView(this);
        presenter.onCreateActivity();
    }

    public void initLayout() {
        setContentView(R.layout.activity_choose_login);
        btnLoginUser = findViewById(R.id.btnLoginUser);
        btnLoginUser.setOnClickListener(this);
        btnLoginGuest = findViewById(R.id.btnLoginGuest);
        btnLoginGuest.setOnClickListener(this);
        btnRegistration = findViewById(R.id.btnRegistration);
        btnRegistration.setOnClickListener(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
