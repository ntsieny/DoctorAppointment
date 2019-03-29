package com.bigblackboy.doctorappointment.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bigblackboy.doctorappointment.R;

public class ChooseLoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLoginUser, btnLoginGuest, btnRegistration;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getSharedPreferences(MainMenuActivity.APP_SETTINGS, MODE_PRIVATE);
        boolean loggedId = mSettings.getBoolean(MainMenuActivity.APP_SETTINGS_USER_LOGGED_IN, false);
        boolean guestMode = mSettings.getBoolean(MainMenuActivity.APP_SETTINGS_GUEST_MODE, false);
        if (loggedId | guestMode) {
            Intent intent = new Intent(this, MainMenuActivity.class);
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
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnLoginUser:
                finish();
                intent = new Intent(this, LoginActivity.class);
                break;
            case R.id.btnRegistration:
                finish();
                intent = new Intent(this, RegistrationActivity.class);
                break;
            case R.id.btnLoginGuest:
                editor = mSettings.edit();
                editor.putBoolean(MainMenuActivity.APP_SETTINGS_USER_LOGGED_IN, false);
                editor.apply();
                finish();
                intent = new Intent(this, MainMenuActivity.class);
                break;
        }
        startActivity(intent);
    }
}
