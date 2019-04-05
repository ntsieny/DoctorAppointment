package com.bigblackboy.doctorappointment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.SpringApi;
import com.bigblackboy.doctorappointment.SpringController;
import com.bigblackboy.doctorappointment.model.Patient;
import com.bigblackboy.doctorappointment.springserver.Response;
import com.bigblackboy.doctorappointment.springserver.springmodel.User;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "myLog: LoginActivity";
    EditText etLogin, etPassword;
    Button btnLogin;
    Patient patient;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    SpringApi springApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        patient = new Patient();

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);

        mSettings = getSharedPreferences(MainMenuActivity.APP_SETTINGS, Context.MODE_PRIVATE);
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
        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_ID, user.getPatient().getServiceId());
        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_NAME, user.getPatient().getName());
        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_LASTNAME, user.getPatient().getLastname());
        editor.putInt(MainMenuActivity.APP_SETTINGS_PATIENT_DAYBIRTH, user.getPatient().getDayBirth());
        editor.putInt(MainMenuActivity.APP_SETTINGS_PATIENT_MONTHBIRTH, user.getPatient().getMonthBirth());
        editor.putInt(MainMenuActivity.APP_SETTINGS_PATIENT_YEARBIRTH, user.getPatient().getYearBirth());
        editor.putString(MainMenuActivity.APP_SETTINGS_DISTRICT_ID, String.valueOf(user.getDistrictId()));
        editor.putString(MainMenuActivity.APP_SETTINGS_DISTRICT_NAME, user.getDistrictName());
        editor.putString(MainMenuActivity.APP_SETTINGS_HOSPITAL_ID, String.valueOf(user.getHospitalId()));
        editor.putString(MainMenuActivity.APP_SETTINGS_HOSPITAL_NAME_SHORT, user.getLpuNameShort());
        editor.putString(MainMenuActivity.APP_SETTINGS_HOSPITAL_NAME_FULL, user.getLpuNameFull());
        editor.putBoolean(MainMenuActivity.APP_SETTINGS_USER_LOGGED_IN, true);
        editor.apply();
    }

    private void openMainMenuActivity() {
        finish();
        Intent intent = new Intent(this, MainMenuActivity.class);
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
                if (response.isSuccessful() && user.getPatient() != null) {
                    Log.d(LOG_TAG, "Пользователь найден");
                    Toast.makeText(LoginActivity.this, "Пользователь " + user.getPatient().getLastname() + " найден", Toast.LENGTH_SHORT).show();
                    writeSharedPreferences(user);
                    // TODO получаем данные пациента и закидываем их через Bundle дальше
                    openMainMenuActivity();
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
