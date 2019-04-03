package com.bigblackboy.doctorappointment.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.Controller;
import com.bigblackboy.doctorappointment.HospitalApi;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.activity.MainMenuActivity;
import com.bigblackboy.doctorappointment.activity.RetrofitResponseListener;
import com.bigblackboy.doctorappointment.api.CheckPatientApiResponse;
import com.bigblackboy.doctorappointment.model.Patient;
import com.bigblackboy.doctorappointment.model.Session;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bigblackboy.doctorappointment.activity.MainMenuActivity.APP_SETTINGS;

public class InputBioFragment extends Fragment implements View.OnClickListener {

    EditText etNameReg, etLastnameReg, etMiddlanameReg;
    TextView tvBirthdayReg;
    Button btnBioReg;
    OnInputBioFragmentDataListener mListener;
    Patient patient;
    private static HospitalApi hospitalApi;
    private static final String LOG_TAG = "myLog";
    private String hospitalId;
    private String districtId;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    public interface OnInputBioFragmentDataListener {
        void onInputBioFragmentDataListener(Patient patient);
    }

    public void setInfo(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnInputBioFragmentDataListener) {
            mListener = (OnInputBioFragmentDataListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnInputBioFragmentDataListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSettings = getActivity().getSharedPreferences(MainMenuActivity.APP_SETTINGS, Context.MODE_PRIVATE);
        hospitalApi = Controller.getApi();

        View v = inflater.inflate(R.layout.fragment_input_bio, container, false);
        etNameReg = v.findViewById(R.id.etNameReg);
        etLastnameReg = v.findViewById(R.id.etLastnameReg);
        etMiddlanameReg = v.findViewById(R.id.etMiddlenameReg);
        tvBirthdayReg = v.findViewById(R.id.tvBirthdayReg);
        tvBirthdayReg.setOnClickListener(this);
        btnBioReg = v.findViewById(R.id.btnBioReg);
        btnBioReg.setOnClickListener(this);
        patient = new Patient();
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBioReg:
                if(!(TextUtils.isEmpty(etNameReg.getText().toString())) && !(TextUtils.isEmpty(etLastnameReg.getText().toString()))) {
                    patient.setName(etNameReg.getText().toString());
                    patient.setLastName(etLastnameReg.getText().toString());
                    patient.setMiddleName(etMiddlanameReg.getText().toString());
                    Log.d(LOG_TAG, patient.toString());
                    checkPatient(new RetrofitResponseListener() {
                        @Override
                        public void onSuccess() {
                            mListener.onInputBioFragmentDataListener(patient);
                            Log.d(LOG_TAG, "Пациент найден");
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(getContext(), "Пациент с такими данными отсутствует в поликлинике. Обратитесь в регистратуру", Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "Пациент с такими данными отсутствует в поликлинике. Обратитесь в регистратуру");
                        }
                    });
                } else Toast.makeText(getContext(), "Введите данные!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvBirthdayReg:
                showDatePicker();
                break;
        }
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            patient.setDayBirth(dayOfMonth);
            patient.setMonthBirth(monthOfYear + 1);
            patient.setYearBirth(year);
            tvBirthdayReg.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year));
        }
    };

    private void checkPatient(final RetrofitResponseListener retrofitResponseListener) {
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

                        /*editor = mSettings.edit();
                        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_ID, patient.getId());
                        editor.putBoolean(MainMenuActivity.APP_SETTINGS_USER_LOGGED_IN, true);
                        editor.apply();*/

                        /*String welcomePhr = "Добро пожаловать, " + patient.getName().substring(0,1).toUpperCase() + patient.getName().substring(1) + "!";
                        Toast.makeText(getContext(), welcomePhr, Toast.LENGTH_LONG).show();*/

                        /*Intent intent = new Intent(getContext(), MainMenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/

                        /*editor = mSettings.edit();
                        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_NAME, patient.getName());
                        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_LASTNAME, patient.getLastName());
                        editor.putString(MainMenuActivity.APP_SETTINGS_PATIENT_BIRTHDAY, patient.getBirthdayFormatted());
                        editor.apply();*/
                        Log.d(LOG_TAG, "Добро пожаловать, " + patient.getName().substring(0,1).toUpperCase() + patient.getName().substring(1) + "!");
                        retrofitResponseListener.onSuccess();
                    }
                    else {
                        Log.d(LOG_TAG, "Ошибка авторизации: " + response.body().getError().getErrorDescription());
                        retrofitResponseListener.onFailure();
                    }
                } else {
                    Log.d(LOG_TAG, "Запрос не прошел (" + response.code() + ")");
                    retrofitResponseListener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<CheckPatientApiResponse> call, Throwable t) {
                Log.d(LOG_TAG, "Error: " + t.getMessage());
                retrofitResponseListener.onFailure();
            }
        });
    }
}
