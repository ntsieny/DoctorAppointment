package com.bigblackboy.doctorappointment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.HospitalApi;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.api.CheckPatientApiResponse;
import com.bigblackboy.doctorappointment.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.fragment.InputBioFragment;
import com.bigblackboy.doctorappointment.fragment.SignUpFragment;
import com.bigblackboy.doctorappointment.model.District;
import com.bigblackboy.doctorappointment.model.Hospital;
import com.bigblackboy.doctorappointment.model.Patient;
import com.bigblackboy.doctorappointment.model.Session;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements DistrictFragment.OnDistrictFragmentDataListener, HospitalFragment.OnHospitalFragmentDataListener,
        SignUpFragment.OnSignUpFragmentDataListener, InputBioFragment.OnInputBioFragmentDataListener {

    FragmentManager fm;
    private Patient patient;
    private static final String LOG_TAG = "myLog";
    private String hospitalId, hospitalName;
    private String districtId, districtName;
    private String login, password;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    FragmentTransaction fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mSettings = getSharedPreferences(MainMenuActivity.APP_SETTINGS, Context.MODE_PRIVATE);

        fm = getSupportFragmentManager();
        DistrictFragment districtFragment = new DistrictFragment();
        fm.beginTransaction().add(R.id.linLayoutRegistration, districtFragment).commit();
    }

    @Override
    public void onDistrictFragmentDataListener(District district) {
        districtId = district.getId();
        districtName = district.getName();
        HospitalFragment hospitalFragment = HospitalFragment.newInstance(district);
        fm.beginTransaction().replace(R.id.linLayoutRegistration, hospitalFragment).addToBackStack("district_fragment").commit();
    }

    @Override
    public void onDistrictUpdateActionBarTitle(String barTitle) {

    }

    @Override
    public void onHospitalFragmentDataListener(Hospital hospital) {
        hospitalId = String.valueOf(hospital.getIdLPU());
        hospitalName = hospital.getLPUShortName();
        SignUpFragment signUpFragment = new SignUpFragment();
        fm.beginTransaction().replace(R.id.linLayoutRegistration, signUpFragment).addToBackStack("hospital_fragment").commit();
    }

    @Override
    public void onHospitalUpdateActionBarTitle(String barTitle) {
    }

    @Override
    public void onSignUpFragmentDataListener(Map<String, String> loginAndPassword) {
        login = loginAndPassword.get("login");
        password = loginAndPassword.get("password");
        InputBioFragment inputBioFragment = new InputBioFragment();
        inputBioFragment.setInfo(hospitalId);
        fm.beginTransaction().replace(R.id.linLayoutRegistration, inputBioFragment).commit();
    }

    @Override
    public void onInputBioFragmentDataListener(Patient patient) {
        String name = patient.getName();
        String lastname = patient.getLastName();
        int dayBirth = patient.getDayBirth();
        int monthBirth = patient.getMonthBirth();
        int yearBirth = patient.getYearBirth();
        String id = patient.getId();

        editor = mSettings.edit();
        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_NAME, name);
        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_LASTNAME, lastname);
        editor.putInt(MainMenuActivity.APP_SETTINGS_PATIENT_DAYBIRTH, dayBirth);
        editor.putInt(MainMenuActivity.APP_SETTINGS_PATIENT_MONTHBIRTH, monthBirth);
        editor.putInt(MainMenuActivity.APP_SETTINGS_PATIENT_YEARBIRTH, yearBirth);
        editor.putString(MainMenuActivity.APP_SETTINGS_DISTRICT_ID, districtId);
        editor.putString(MainMenuActivity.APP_SETTINGS_DISTRICT_NAME, districtName);
        editor.putString(MainMenuActivity.APP_SETTINGS_HOSPITAL_ID, hospitalId);
        editor.putString(MainMenuActivity.APP_SETTINGS_HOSPITAL_NAME, hospitalName);
        editor.putBoolean(MainMenuActivity.APP_SETTINGS_USER_LOGGED_IN, true);
        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_ID, id);
        editor.apply();

        //запрос на создание профиля пользователя
        // ...
        finish();
        // открытие окна профиля/главное меню
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);

    }
}
