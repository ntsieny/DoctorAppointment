package com.bigblackboy.doctorappointment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.Patient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etLogin, etPassword;
    Button btnLogin;
    Patient patient;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if(!TextUtils.isEmpty(etLogin.getText().toString()) && !TextUtils.isEmpty(etPassword.getText().toString())) {
                    Toast.makeText(this, "Проверка данных...", Toast.LENGTH_SHORT).show();
                    // TODO проверка существования пользователя в БД...
                    // если существует, то
                    editor = mSettings.edit();
                    editor.putBoolean(MainMenuActivity.APP_SETTINGS_USER_LOGGED_IN, true);
                    editor.apply();
                    // получаем данные пациента и закидываем их через Bundle дальше
                    Intent intent = new Intent(this, MainMenuActivity.class);
                    startActivity(intent);
                    // иначе выдается ошибка
                } else Toast.makeText(this, "Введите данные", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
