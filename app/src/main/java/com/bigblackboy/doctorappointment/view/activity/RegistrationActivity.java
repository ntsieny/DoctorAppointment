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
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fm = getSupportFragmentManager();
        addDistrictFragment();

        SharedPreferences mSettings = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        presenter = new RegistrationActivityPresenter(mSettings);
        presenter.attachView(this);
    }

    @Override
    public void onDistrictFragmentDataListener(District district) {
        presenter.onGetDistrictFragmentData(district);
    }

    @Override
    public void onHospitalFragmentDataListener(Hospital hospital) {
        presenter.onGetHospitalFragmentData(hospital);
    }

    @Override
    public void onSignUpFragmentDataListener(Map<String, String> loginAndPassword) {
        presenter.onGetSignUpFragmentData(loginAndPassword);
    }

    @Override
    public void onInputBioFragmentDataListener(Patient patient) {
        presenter.onGetInputBioFragmentData(patient);
    }

    public void openMainMenuActivity() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void replaceToHospitalFragment(District district) {
        HospitalFragment hospitalFragment = HospitalFragment.newInstance(district);
        fm.beginTransaction().replace(R.id.linLayoutRegistration, hospitalFragment).addToBackStack("district_fragment").commit();
    }

    public void replaceToSignUpFragment() {
        SignUpFragment signUpFragment = new SignUpFragment();
        fm.beginTransaction().replace(R.id.linLayoutRegistration, signUpFragment).addToBackStack("hospital_fragment").commit();
    }

    public void replaceToInputBioFragment(String hospitalId) {
        InputBioFragment inputBioFragment = InputBioFragment.newInstance(hospitalId);
        fm.beginTransaction().replace(R.id.linLayoutRegistration, inputBioFragment).addToBackStack("signup_fragment").commit();
    }

    private void addDistrictFragment() {
        DistrictFragment districtFragment = new DistrictFragment();
        fm.beginTransaction().add(R.id.linLayoutRegistration, districtFragment).commit();
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
