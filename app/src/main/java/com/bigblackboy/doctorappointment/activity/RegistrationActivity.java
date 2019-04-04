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
import com.bigblackboy.doctorappointment.SpringApi;
import com.bigblackboy.doctorappointment.SpringController;
import com.bigblackboy.doctorappointment.api.CheckPatientApiResponse;
import com.bigblackboy.doctorappointment.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.fragment.InputBioFragment;
import com.bigblackboy.doctorappointment.fragment.SignUpFragment;
import com.bigblackboy.doctorappointment.model.District;
import com.bigblackboy.doctorappointment.model.Hospital;
import com.bigblackboy.doctorappointment.model.Patient;
import com.bigblackboy.doctorappointment.model.Session;
import com.bigblackboy.doctorappointment.springserver.Response;
import com.bigblackboy.doctorappointment.springserver.springmodel.User;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class RegistrationActivity extends AppCompatActivity implements DistrictFragment.OnDistrictFragmentDataListener, HospitalFragment.OnHospitalFragmentDataListener,
        SignUpFragment.OnSignUpFragmentDataListener, InputBioFragment.OnInputBioFragmentDataListener {

    SpringApi springApi;
    FragmentManager fm;
    private Patient patient;
    private static final String LOG_TAG = "myLog";
    private Hospital hospital;
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

        springApi = SpringController.getApi();
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
        this.hospital = hospital;
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
        inputBioFragment.setInfo(String.valueOf(hospital.getIdLPU()));
        fm.beginTransaction().replace(R.id.linLayoutRegistration, inputBioFragment).addToBackStack("signup_fragment").commit();
    }

    @Override
    public void onInputBioFragmentDataListener(Patient patient) {
        String name = patient.getName();
        String lastname = patient.getLastName();
        int dayBirth = patient.getDayBirth();
        int monthBirth = patient.getMonthBirth();
        int yearBirth = patient.getYearBirth();
        String id = patient.getId();

        // TODO запрос на создание профиля пользователя
        User user = new User();
        user.setId(0);
        user.setLogin(login);
        user.setPassword(password);
        user.setPatientId(0);
        user.setDistrictId(Integer.parseInt(districtId));
        user.setDistrictName(districtName);
        user.setHospitalId(hospital.getIdLPU());
        user.setLpuNameShort(hospital.getLPUShortName());
        user.setLpuNameFull(hospital.getLpuName());
        user.setLpuAddress(hospital.getAddress());
        user.setLpuEmail(hospital.getEmail());
        user.setLpuType(hospital.getLpuType());
        user.setLpuWorkTime(hospital.getWorkTime());
        com.bigblackboy.doctorappointment.springserver.springmodel.Patient p = new com.bigblackboy.doctorappointment.springserver.springmodel.Patient();
        p.setName(name);
        p.setLastname(lastname);
        p.setMiddlename(patient.getMiddleName());
        p.setDayBirth(dayBirth);
        p.setMonthBirth(monthBirth);
        p.setYearBirth(yearBirth);
        p.setServiceId(id);
        user.setPatient(p);

        createUser(user);

        // TODO ПРОВЕРКА УСПЕШНОСТИ ЗАПРОСА
        editor = mSettings.edit();
        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_NAME, name);
        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_LASTNAME, lastname);
        editor.putInt(MainMenuActivity.APP_SETTINGS_PATIENT_DAYBIRTH, dayBirth);
        editor.putInt(MainMenuActivity.APP_SETTINGS_PATIENT_MONTHBIRTH, monthBirth);
        editor.putInt(MainMenuActivity.APP_SETTINGS_PATIENT_YEARBIRTH, yearBirth);
        editor.putString(MainMenuActivity.APP_SETTINGS_DISTRICT_ID, districtId);
        editor.putString(MainMenuActivity.APP_SETTINGS_DISTRICT_NAME, districtName);
        editor.putString(MainMenuActivity.APP_SETTINGS_HOSPITAL_ID, String.valueOf(hospital.getIdLPU()));
        editor.putString(MainMenuActivity.APP_SETTINGS_HOSPITAL_NAME_SHORT, hospital.getLPUShortName());
        editor.putString(MainMenuActivity.APP_SETTINGS_HOSPITAL_NAME_FULL, hospital.getLpuName());
        editor.putBoolean(MainMenuActivity.APP_SETTINGS_USER_LOGGED_IN, true);
        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_ID, id);
        editor.apply();

        finish();
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);

        // если запрос не удался
        //...

    }

    private void createUser(User user) {
        springApi.createUser(user).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response resp = response.body();
                if (resp.isSuccess()) {
                    Log.d(LOG_TAG, "Пользователь создан");
                }
                else {
                    Log.d(LOG_TAG, "Пользователь не создан. " + resp.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }
}
