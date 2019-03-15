package com.bigblackboy.doctorappointment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.Controller;
import com.bigblackboy.doctorappointment.HospitalApi;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.api.CheckPatientApiResponse;
import com.bigblackboy.doctorappointment.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.fragment.LoginFragment;
import com.bigblackboy.doctorappointment.model.Patient;
import com.bigblackboy.doctorappointment.model.Session;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserDataActivity extends AppCompatActivity implements View.OnClickListener, OnDataPass {

    private static HospitalApi hospitalApi;
    private Patient patient;
    private static final String LOG_TAG = "myLog";
    private String hospitalId;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    FragmentTransaction fTrans;
    FragmentManager fm;
    DistrictFragment districtFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hospitalApi = Controller.getApi();
        patient = new Patient();
        mSettings = getSharedPreferences(MainMenuActivity.APP_SETTINGS, Context.MODE_PRIVATE);
        fm = getSupportFragmentManager();
        districtFragment = new DistrictFragment();
        fm.beginTransaction().add(R.id.linLayoutLogin, districtFragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.loginBtn:
                getMetadata();
                break;*/
            /*case R.id.showSpecialitiesBtn:
                getSpecialities();
                break;*/
            /*case R.id.showDoctorsBtn:
                getDoctors();
                break;*/
            /*case R.id.showAppointmentsBtn:
                getAppointments();
                break;*/
        }
    }


    private void checkPatient() {
        hospitalApi.getMetadata(patient.getName(), patient.getLastName(), patient.getMiddleName(), patient.getInsuranceSeries(), patient.getInsuranceNumber(),
                patient.getBirthdayFormatted(), hospitalId, "").enqueue(new Callback<CheckPatientApiResponse>() {
            @Override
            public void onResponse(Call<CheckPatientApiResponse> call, Response<CheckPatientApiResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getSuccess()) {
                        Session session = new Session();

                        CheckPatientApiResponse respObj = response.body();
                        patient.setId(respObj.getResponse().getPatientId());
                        //dataHashMap.put("patient_id", respObj.getResponse().getPatientId());

                        String cookie = response.headers().get("Set-Cookie");
                        if (cookie != null) session.setCookies(cookie);
                        Log.d("myLog", "patientId: " + patient.getId() + ", cookie: " + cookie);

                        editor = mSettings.edit();
                        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_ID, patient.getId());
                        editor.putBoolean(MainMenuActivity.APP_SETTINGS_USER_LOGGED_IN, true);
                        editor.apply();

                        String welcomePhr = "Добро пожаловать, " + patient.getName().substring(0,1).toUpperCase() + patient.getName().substring(1) + "!";
                        Toast.makeText(GetUserDataActivity.this, welcomePhr , Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        editor = mSettings.edit();
                        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_NAME, patient.getName());
                        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_LASTNAME, patient.getLastName());
                        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_BIRTHDAY, patient.getBirthdayFormatted());
                        editor.apply();
                    }
                    else {
                        //Log.d(LOG_TAG, "Ошибка авторизации: " + response.body().getError().getErrorDescription());
                        Toast.makeText(GetUserDataActivity.this, "Ошибка авторизации: " + response.body().getError().getErrorDescription(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    //Log.d(LOG_TAG, "Запрос не прошел (" + response.code() + ")");
                    Toast.makeText(GetUserDataActivity.this, "Запрос не прошел (" + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CheckPatientApiResponse> call, Throwable t) {
                Toast.makeText(GetUserDataActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDataPass(int fragmentId, HashMap<String, String> hashMap) {
        Bundle bundle;
        switch (fragmentId) {
            case 1:
                // район
                fTrans = fm.beginTransaction();
                HospitalFragment hospitalFragment = new HospitalFragment();

                bundle = new Bundle();
                bundle.putSerializable("hashmap", hashMap);
                hospitalFragment.setArguments(bundle);
                fTrans.replace(R.id.linLayoutLogin, hospitalFragment).addToBackStack("fragment_district");
                fTrans.commit();
                break;
            case 2:
                hospitalId = hashMap.get("hospital_id");
                fTrans = fm.beginTransaction();
                LoginFragment loginFragment = new LoginFragment();

                bundle = new Bundle();
                bundle.putSerializable("hashmap", hashMap);
                loginFragment.setArguments(bundle);
                fTrans.replace(R.id.linLayoutLogin, loginFragment).addToBackStack("fragment_hospital");
                fTrans.commit();
                break;
            case 3:
                patient.setName(hashMap.get("name"));
                patient.setLastName(hashMap.get("lastname"));
                patient.setMiddleName("");
                patient.setInsuranceSeries("");
                patient.setInsuranceNumber("");
                patient.setDayBirth(Integer.parseInt(hashMap.get("daybirth")));
                patient.setMonthBirth(Integer.parseInt(hashMap.get("monthbirth")));
                patient.setYearBirth(Integer.parseInt(hashMap.get("yearbirth")));
                checkPatient();
                break;
        }
    }
}
