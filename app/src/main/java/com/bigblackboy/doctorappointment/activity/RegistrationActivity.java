package com.bigblackboy.doctorappointment.activity;

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
    private String hospitalId;
    private String districtId;
    private String login, password;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    FragmentTransaction fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fm = getSupportFragmentManager();
        DistrictFragment districtFragment = new DistrictFragment();
        fm.beginTransaction().add(R.id.linLayoutRegistration, districtFragment).commit();
    }

    @Override
    public void onDistrictFragmentDataListener(District district) {
        districtId = district.getId();
        HospitalFragment hospitalFragment = new HospitalFragment();
        hospitalFragment.setDistrict(district);
        fm.beginTransaction().replace(R.id.linLayoutRegistration, hospitalFragment).addToBackStack("district_fragment").commit();
    }

    @Override
    public void onHospitalFragmentDataListener(Hospital hospital) {
        hospitalId = String.valueOf(hospital.getIdLPU());
        SignUpFragment signUpFragment = new SignUpFragment();
        fm.beginTransaction().replace(R.id.linLayoutRegistration, signUpFragment).addToBackStack("hospital_fragment").commit();
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
        //запрос на создание профиля пользователя
        // ...
        // открытие окна профиля/главное меню
    }


}
