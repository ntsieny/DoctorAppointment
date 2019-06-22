package com.bigblackboy.doctorappointment.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.pojos.springpojos.User;

import retrofit2.Call;
import retrofit2.Callback;

import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_DISTRICT_ID;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_DISTRICT_NAME;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_ID;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_NAME_FULL;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_NAME_SHORT;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_PATIENT_DAYBIRTH;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_PATIENT_ID;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_PATIENT_LASTNAME;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_PATIENT_MIDDLENAME;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_PATIENT_MONTHBIRTH;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_PATIENT_NAME;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_PATIENT_YEARBIRTH;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_USER_LOGGED_IN;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "myLog: LoginActivity";
    private EditText etLogin, etPassword;
    private Button btnLogin;
    private Patient patient;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;
    private SpringApi springApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        patient = new Patient();

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);

        mSettings = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        springApi = SpringController.getApi();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if(!TextUtils.isEmpty(etLogin.getText().toString()) && !TextUtils.isEmpty(etPassword.getText().toString())) {
                    checkUserByLoginPassword(etLogin.getText().toString(), etPassword.getText().toString());
                } else Toast.makeText(this, "Введите данные", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void writeSharedPreferences(User user) {
        editor = mSettings.edit();
        editor.putString(APP_SETTINGS_PATIENT_ID, user.getServiceId());
        editor.putString(APP_SETTINGS_PATIENT_NAME, user.getName());
        editor.putString(APP_SETTINGS_PATIENT_LASTNAME, user.getLastname());
        editor.putString(APP_SETTINGS_PATIENT_MIDDLENAME, user.getMiddlename());
        editor.putInt(APP_SETTINGS_PATIENT_DAYBIRTH, user.getDayBirth());
        editor.putInt(APP_SETTINGS_PATIENT_MONTHBIRTH, user.getMonthBirth());
        editor.putInt(APP_SETTINGS_PATIENT_YEARBIRTH, user.getYearBirth());
        editor.putString(APP_SETTINGS_DISTRICT_ID, String.valueOf(user.getDistrictId()));
        editor.putString(APP_SETTINGS_DISTRICT_NAME, user.getDistrictName());
        editor.putInt(APP_SETTINGS_HOSPITAL_ID, user.getHospitalId());
        editor.putString(APP_SETTINGS_HOSPITAL_NAME_SHORT, user.getLpuNameShort());
        editor.putString(APP_SETTINGS_HOSPITAL_NAME_FULL, user.getLpuNameFull());
        editor.putBoolean(APP_SETTINGS_USER_LOGGED_IN, true);
        editor.apply();
    }

    private void openMainActivity() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void checkUserByLoginPassword(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        springApi.getUserByLoginPassword(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                User user = response.body();
                if (response.isSuccessful() && user != null) {
                    Log.d(LOG_TAG, "Пользователь найден");
                    writeSharedPreferences(user);
                    openMainActivity();
                    Toast.makeText(LoginActivity.this, String.format("Добро пожаловать, %s %s!", user.getName(), user.getMiddlename()), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Пользователь не найден");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }
}
