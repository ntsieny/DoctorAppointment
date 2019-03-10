package com.bigblackboy.doctorappointment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.fragment.AppointmentFragment;
import com.bigblackboy.doctorappointment.fragment.DistrictFragment;

public class  MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDistricts;
    public static final String APP_SETTINGS = "app_settings";
    public static final String APP_SETTINGS_USER_LOGGED_IN = "user_logged_in";
    public static final String APP_SETTINGS_DISTRICT_ID = "district_id";
    public static final String APP_SETTINGS_HOSPITAL_ID = "hospital_id";
    public static final String APP_SETTINGS_PATIENT_ID = "patient_id";
    public static final String APP_SETTINGS_PATIENT_NAME = "patient_name";
    public static final String APP_SETTINGS_PATIENT_LASTNAME = "patient_lastname";
    public static final String APP_SETTINGS_PATIENT_BIRTHDAY = "patient_birthday";
    SharedPreferences mSettings;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        if (mSettings.getBoolean(APP_SETTINGS_USER_LOGGED_IN, false)) {
            setContentView(R.layout.activity_main);
            btnDistricts = findViewById(R.id.btnDistricts);
            btnDistricts.setOnClickListener(this);
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutItem:
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean(APP_SETTINGS_USER_LOGGED_IN, false);
                editor.apply();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnDistricts:
                intent = new Intent(this, MakeAppointmentActivity.class);
                startActivity(intent);
                break;
        }
    }
}
