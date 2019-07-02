package com.bigblackboy.doctorappointment.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.UserModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.pojos.springpojos.User;
import com.bigblackboy.doctorappointment.presenter.RegistrationActivityPresenter;
import com.bigblackboy.doctorappointment.view.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.view.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.view.fragment.InputBioFragment;
import com.bigblackboy.doctorappointment.view.fragment.SignUpFragment;

import java.util.Map;

import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS;

public class RegistrationActivity extends AppCompatActivity implements MVPBaseInterface.View, DistrictFragment.OnDistrictFragmentDataListener, HospitalFragment.OnHospitalFragmentDataListener,
        SignUpFragment.OnSignUpFragmentDataListener, InputBioFragment.OnInputBioFragmentDataListener {

    private RegistrationActivityPresenter presenter;
    private static final String LOG_TAG = "myLog: RegActivity";
    private FragmentManager fm;
    private Patient patient;
    private Hospital hospital;
    private String districtId, districtName;
    private String login, password;
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mSettings = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);

        fm = getSupportFragmentManager();
        DistrictFragment districtFragment = new DistrictFragment();
        fm.beginTransaction().add(R.id.linLayoutRegistration, districtFragment).commit();

        UserModel userModel = new UserModel(mSettings);
        presenter = new RegistrationActivityPresenter(userModel);
        presenter.attachView(this);
    }

    @Override
    public void onDistrictFragmentDataListener(District district) {
        districtId = district.getId();
        districtName = district.getName();
        HospitalFragment hospitalFragment = HospitalFragment.newInstance(district);
        fm.beginTransaction().replace(R.id.linLayoutRegistration, hospitalFragment).addToBackStack("district_fragment").commit();
    }

    @Override
    public void onHospitalFragmentDataListener(Hospital hospital) {
        this.hospital = hospital;
        SignUpFragment signUpFragment = new SignUpFragment();
        fm.beginTransaction().replace(R.id.linLayoutRegistration, signUpFragment).addToBackStack("hospital_fragment").commit();
    }

    @Override
    public void onSignUpFragmentDataListener(Map<String, String> loginAndPassword) {
        login = loginAndPassword.get("login");
        password = loginAndPassword.get("password");
        InputBioFragment inputBioFragment = InputBioFragment.newInstance(String.valueOf(hospital.getIdLPU()));
        fm.beginTransaction().replace(R.id.linLayoutRegistration, inputBioFragment).addToBackStack("signup_fragment").commit();
    }

    @Override
    public void onInputBioFragmentDataListener(Patient patient) {
        this.patient = patient;
        presenter.createUser();
    }

    public User createUserObjectForRequest() {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setPatientId(0);
        user.setDistrictId(Integer.parseInt(districtId));
        user.setDistrictName(districtName);
        user.setHospitalId(hospital.getIdLPU());
        user.setLpuNameShort(hospital.getLPUShortName());
        user.setLpuNameFull(hospital.getLpuName());
        user.setLpuAddress(hospital.getFullAddress());
        user.setLpuEmail(hospital.getEmail());
        user.setLpuType(hospital.getLpuType());
        user.setLpuWorkTime(hospital.getWorkTime());
        user.setName(patient.getName());
        user.setLastname(patient.getLastName());
        user.setMiddlename(patient.getMiddleName());
        user.setDayBirth(patient.getDayBirth());
        user.setMonthBirth(patient.getMonthBirth());
        user.setYearBirth(patient.getYearBirth());
        user.setServiceId(patient.getServiceId());
        return user;
    }

    public void openMainMenuActivity() {
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

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
