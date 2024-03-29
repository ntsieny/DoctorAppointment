package com.bigblackboy.doctorappointment.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.UserModel;
import com.bigblackboy.doctorappointment.presenter.LoginActivityPresenter;

import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS;

public class LoginActivity extends AppCompatActivity implements MVPBaseInterface.View, View.OnClickListener {

    private LoginActivityPresenter presenter;
    private static final String LOG_TAG = "myLog: LoginActivity";
    private EditText etLogin, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);

        SharedPreferences mSettings = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        presenter = new LoginActivityPresenter(mSettings);
        presenter.attachView(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                presenter.onBtnLoginClick(etLogin.getText().toString(), etPassword.getText().toString());
                break;
        }
    }

    public void openMainActivity() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        //
    }

    @Override
    public void hideProgressBar() {
        //
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
