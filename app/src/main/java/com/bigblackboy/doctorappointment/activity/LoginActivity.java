package com.bigblackboy.doctorappointment.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.Controller;
import com.bigblackboy.doctorappointment.HospitalApi;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.api.AppointmentListApiResponse;
import com.bigblackboy.doctorappointment.api.DoctorsApiResponse;
import com.bigblackboy.doctorappointment.api.Error;
import com.bigblackboy.doctorappointment.model.AppointmentInfo;
import com.bigblackboy.doctorappointment.model.Doctor;
import com.bigblackboy.doctorappointment.model.Speciality;
import com.bigblackboy.doctorappointment.api.SpecialitiesApiResponse;
import com.bigblackboy.doctorappointment.api.CheckPatientApiResponse;
import com.bigblackboy.doctorappointment.model.Patient;
import com.bigblackboy.doctorappointment.model.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText surnameET;
    EditText nameET;
    TextView birthdayTV;
    TextView infoTV;
    Button loginBtn;
    Button showSpecialitiesBtn;
    Button showDoctorsBtn;
    Button showAppointments;
    int DIALOG_DATE = 1;
    private static HospitalApi hospitalApi;
    Patient patient;
    private static final String LOG_TAG = "LoginActivityLOG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        surnameET = findViewById(R.id.surnameET);
        nameET = findViewById(R.id.nameET);
        birthdayTV = findViewById(R.id.birthdayTV);
        infoTV = findViewById(R.id.infoTV);

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);

        showSpecialitiesBtn = findViewById(R.id.showSpecialitiesBtn);
        showSpecialitiesBtn.setOnClickListener(this);

        showDoctorsBtn = findViewById(R.id.showDoctorsBtn);
        showDoctorsBtn.setOnClickListener(this);

        showAppointments = findViewById(R.id.showAppointmentsBtn);
        showAppointments.setOnClickListener(this);

        hospitalApi = Controller.getApi();
        patient = new Patient();
    }

    public void onBirthdayClick(View v) {
        showDialog(DIALOG_DATE);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_DATE) {
            DatePickerDialog datePD = new DatePickerDialog(this, myCallBack, 1997, 10, 25);
            return datePD;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            patient.setDayBirth(dayOfMonth);
            patient.setMonthBirth(month);
            patient.setYearBirth(year);
            birthdayTV.setText(dayOfMonth + "." + (month + 1) + "." + year);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                getMetadata();
                break;
            case R.id.showSpecialitiesBtn:
                getSpecialities();
                break;
            case R.id.showDoctorsBtn:
                getDoctors();
                break;
            case R.id.showAppointmentsBtn:
                getAppointments();
                break;
        }
    }


    private void getMetadata() {
        patient.setName(nameET.getText().toString());
        patient.setLastName(surnameET.getText().toString());
        String hospitalId = "400";
        //patient.setMiddleName("");
        //patient.setInsuranceSeries("");
        //patient.setInsuranceNumber("");

        hospitalApi.getMetadata(patient.getName(), patient.getLastName(), patient.getMiddleName(), patient.getInsuranceSeries(), patient.getInsuranceNumber(),
                                patient.getBirthdayFormatted(), hospitalId, "").enqueue(new Callback<CheckPatientApiResponse>() {
            @Override
            public void onResponse(Call<CheckPatientApiResponse> call, Response<CheckPatientApiResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getSuccess()) {
                        Session session = new Session();

                        CheckPatientApiResponse respObj = response.body();
                        patient.setId(respObj.getResponse().getPatientId());

                        String cookie = response.headers().get("Set-Cookie");
                        if (cookie != null) session.setCookies(cookie);
                        String welcomePhr = "Добро пожаловать, " + patient.getName().substring(0,1).toUpperCase() + patient.getName().substring(1) + "!";
                        Log.d(LOG_TAG, welcomePhr);
                        Toast.makeText(LoginActivity.this, welcomePhr , Toast.LENGTH_LONG).show();
                    }
                    else {
                        Log.d(LOG_TAG, "Ошибка авторизации: " + response.body().getError().getErrorDescription());
                        Toast.makeText(LoginActivity.this, "Ошибка авторизации: " + response.body().getError().getErrorDescription(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(LOG_TAG, "Запрос не прошел (" + response.code() + ")");
                    Toast.makeText(LoginActivity.this, "Запрос не прошел (" + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CheckPatientApiResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getSpecialities() {
        hospitalApi.getSpecialities("400", "", "", patient.getId()).enqueue(new Callback<SpecialitiesApiResponse>() {
            @Override
            public void onResponse(Call<SpecialitiesApiResponse> call, Response<SpecialitiesApiResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getSuccess()) {
                        SpecialitiesApiResponse respObj = response.body();
                        Log.d(LOG_TAG, "--- СПИСОК СПЕЦИАЛЬНОСТЕЙ ---\n");
                        for(Speciality speciality : respObj.getSpecialities()) {
                            Log.d(LOG_TAG, speciality.getNameSpesiality() + "\n" +
                                                speciality.getLastDate().getDateTime() + "\n" +
                                                speciality.getNearestDate().getDateTime() + "\n" +
                                                speciality.getCountFreeParticipants() + "\n" + // доступно номерков
                                                speciality.getCountFreeTickets() + "\n" + // доступно номерков
                                                speciality.getFerIdSpeciality() + "\n" +
                                                speciality.getIdSpeciality() + "\n"); // передается в form-data при выборе доктора
                        }
                    }
                    else Toast.makeText(LoginActivity.this, "Ошибка: " + response.body().getError().getErrorDescription(), Toast.LENGTH_LONG).show();
                } else Toast.makeText(LoginActivity.this, "Запрос не прошел (" + response.code() + ")", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<SpecialitiesApiResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDoctors() {
        hospitalApi.getDoctors("1122", "400", "037600000794946", "").enqueue(new Callback<DoctorsApiResponse>() {
            @Override
            public void onResponse(Call<DoctorsApiResponse> call, Response<DoctorsApiResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getSuccess()) {
                        DoctorsApiResponse respObj = response.body();
                        Log.d(LOG_TAG, "--- СПИСОК ДОКТОРОВ ---\n");
                        for (Doctor doctor : respObj.getDoctors()) {
                            Log.d(LOG_TAG, doctor.getName() + " Участок: " + doctor.getAriaNumber() + " СНИЛС: " + doctor.getSnils() + "\n");
                            Log.d(LOG_TAG, doctor.getIdDoc()); // сохранить для form-data
                        }
                    }
                    else infoTV.append("Ошибка: " + response.body().getError().getErrorDescription());
                }
                else infoTV.append("Запрос не прошел (" + response.code() + ")");
            }

            @Override
            public void onFailure(Call<DoctorsApiResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAppointments() {
        hospitalApi.getAppointments("3235", "400", "037600000794946", "", "").enqueue(new Callback<AppointmentListApiResponse>() {
            @Override
            public void onResponse(Call<AppointmentListApiResponse> call, Response<AppointmentListApiResponse> response) {
                if (response.isSuccessful()) {

                    AppointmentListApiResponse respObj = response.body();
                    List<AppointmentInfo> infoList;
                    Map<String, List<AppointmentInfo>> resp = respObj.getResponse();
                    Log.d(LOG_TAG, "--- СПИСОК ВРЕМЕНИ ПРИЕМА ---\n");
                    for (Map.Entry entry : resp.entrySet()) {
                        Log.d(LOG_TAG, "Дата: " + entry.getKey() + "\n");
                        infoList = (List<AppointmentInfo>) entry.getValue();
                        for (AppointmentInfo info : infoList) {
                            Log.d(LOG_TAG, info.toString() + "\n");
                        }
                    }

                    /*Log.w("Full JSON:\n", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                    Gson gson = new Gson();
                    String json = new GsonBuilder().create().toJson(response.body());
                    AppointmentListApiResponse apire = gson.fromJson(json, AppointmentListApiResponse.class);
                    Error error = apire.getError();
                    boolean success = apire.getSuccess();
                    Map<String, List<AppointmentInfo>> responseMap = apire.getResponse();

                    List<AppointmentInfo> infoList;
                    for (Map.Entry entry : responseMap.entrySet()) {
                        infoTV.append("Дата: " + entry.getKey() + "\n");
                        infoList = (List<AppointmentInfo>) entry.getValue();
                        for (AppointmentInfo info : infoList) {
                            infoTV.append(info.toString());
                        }
                        infoTV.append("\n");
                    }*/


                    /*// и это тоже работает!
                    Type tp = new TypeToken<AppointmentListApiResponse>(){}.getType();
                    AppointmentListApiResponse rrr = gson.fromJson(json, tp);

                    Map<String, Object> responseMap = new HashMap<>();
                    responseMap = (Map<String, Object>) gson.fromJson(json, responseMap.getClass());
                    Map datesTreeMap = (LinkedTreeMap) responseMap.get("response");

                    *//*Set set = datesTreeMap.entrySet();
                    Iterator it = set.iterator();
                    Map datesHashMap = new HashMap();
                    while (it.hasNext()) {
                        Map.Entry me = (Map.Entry) it.next();
                        datesHashMap.put(me.getKey(), me.getValue());
                    }*//*

                    //JsonElement jsonElement = gson.toJsonTree(datesHashMap);
                    JsonElement jsonElement = gson.toJsonTree(datesTreeMap);

                    //магия
                    Type type = new TypeToken<Map<String, List<AppointmentInfo>>>(){}.getType();
                    AppointmentInfo obj = gson.fromJson(jsonElement, type);

                    LinkedTreeMap successMap = (LinkedTreeMap) responseMap.get("success");
                    LinkedTreeMap errorMap = (LinkedTreeMap) responseMap.get("error");*/
                }
                else {
                    Toast.makeText(LoginActivity.this,"Запрос не прошел (" + response.code() + ")", Toast.LENGTH_LONG).show();
                    Log.w("Full JSON:\n", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                }
            }

            @Override
            public void onFailure(Call<AppointmentListApiResponse> call, Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void createAppointment() {

    }

    /*///
    /// Поискать инфу на тему interceptor, сохранение cookie для последующего использования
    /// */
}
