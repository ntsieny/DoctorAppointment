package com.bigblackboy.doctorappointment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.fragment.InputBioFragment;
import com.bigblackboy.doctorappointment.fragment.SignUpFragment;
import com.bigblackboy.doctorappointment.model.District;
import com.bigblackboy.doctorappointment.model.Hospital;
import com.bigblackboy.doctorappointment.model.Patient;
import com.bigblackboy.doctorappointment.springserver.Response;
import com.bigblackboy.doctorappointment.springserver.springmodel.User;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_DISTRICT_ID;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_DISTRICT_NAME;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_ID;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_NAME_FULL;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_NAME_SHORT;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_PATIENT_DAYBIRTH;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_PATIENT_ID;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_PATIENT_LASTNAME;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_PATIENT_MIDDLENAME;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_PATIENT_MONTHBIRTH;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_PATIENT_NAME;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_PATIENT_YEARBIRTH;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_USER_LOGGED_IN;

public class RegistrationActivity extends AppCompatActivity implements DistrictFragment.OnDistrictFragmentDataListener, HospitalFragment.OnHospitalFragmentDataListener,
        SignUpFragment.OnSignUpFragmentDataListener, InputBioFragment.OnInputBioFragmentDataListener {

    private static final String LOG_TAG = "myLog: RegActivity";
    SpringApi springApi;
    FragmentManager fm;
    private Patient patient;
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

        mSettings = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);

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
        createUser(createUserObjectForRequest());
    }

    private User createUserObjectForRequest() {
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

    private void writeSharedPreferences() {
        editor = mSettings.edit();
        editor.putString(APP_SETTINGS_PATIENT_ID, patient.getServiceId());
        editor.putString(APP_SETTINGS_PATIENT_NAME, patient.getName());
        editor.putString(APP_SETTINGS_PATIENT_LASTNAME, patient.getLastName());
        editor.putString(APP_SETTINGS_PATIENT_MIDDLENAME, patient.getMiddleName());
        editor.putInt(APP_SETTINGS_PATIENT_DAYBIRTH, patient.getDayBirth());
        editor.putInt(APP_SETTINGS_PATIENT_MONTHBIRTH, patient.getMonthBirth());
        editor.putInt(APP_SETTINGS_PATIENT_YEARBIRTH, patient.getYearBirth());
        editor.putString(APP_SETTINGS_DISTRICT_ID, districtId);
        editor.putString(APP_SETTINGS_DISTRICT_NAME, districtName);
        editor.putInt(APP_SETTINGS_HOSPITAL_ID, hospital.getIdLPU());
        editor.putString(APP_SETTINGS_HOSPITAL_NAME_SHORT, hospital.getLPUShortName());
        editor.putString(APP_SETTINGS_HOSPITAL_NAME_FULL, hospital.getLpuName());
        editor.putBoolean(APP_SETTINGS_USER_LOGGED_IN, true);
        editor.apply();
    }

    private void openMainMenuAcvitity() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void createUser(final User user) {
        springApi.createUser(user).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response resp = response.body();
                if (response.isSuccessful()) {
                    if (resp.isSuccess()) {
                        Log.d(LOG_TAG, "Пользователь создан");
                        writeSharedPreferences();
                        openMainMenuAcvitity();
                        Toast.makeText(RegistrationActivity.this, String.format("Добро пожаловать, %s!", user.getName()), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d(LOG_TAG, "Пользователь не создан. " + resp.getMessage());
                    }
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(RegistrationActivity.this, error.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }
}
