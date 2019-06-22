package com.bigblackboy.doctorappointment.view.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
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

import com.bigblackboy.doctorappointment.controller.HospitalController;
import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.api.CheckPatientApiResponse;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS;

public class InputBioFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private static final String LOG_TAG = "myLog: InputBioFragment";
    EditText etNameReg, etLastnameReg, etMiddlanameReg;
    TextView tvBirthdayReg;
    Button btnBioReg;
    OnInputBioFragmentDataListener mListener;
    Patient patient;
    private static HospitalApi hospitalApi;
    private String hospitalId;
    private String districtId;
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    public interface OnInputBioFragmentDataListener {
        void onInputBioFragmentDataListener(Patient patient);
    }

    public static InputBioFragment newInstance(String hospitalId) {
        InputBioFragment inputBioFragment = new InputBioFragment();
        Bundle args = new Bundle();
        args.putString("hospital_id", hospitalId);
        inputBioFragment.setArguments(args);
        return inputBioFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnInputBioFragmentDataListener) {
            mListener = (OnInputBioFragmentDataListener) context;
        } else throw new RuntimeException(context.toString() + " must implement OnInputBioFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hospitalId = getArguments().getString("hospital_id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSettings = getActivity().getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        hospitalApi = HospitalController.getApi();

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
                    checkPatient(patient.getName(), patient.getLastName(), patient.getMiddleName());
                } else Toast.makeText(getContext(), "Введите данные!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvBirthdayReg:
                showDatePicker();
                break;
        }
    }

    private void showDatePicker() {
        DatePickerFragment datePickerFrag;
        if (patient.getDayBirth() != 0 && patient.getMonthBirth() != 0 && patient.getYearBirth() != 0) {
            datePickerFrag = DatePickerFragment.newInstance(patient.getYearBirth(), patient.getMonthBirth() - 1, patient.getDayBirth());
        } else datePickerFrag = new DatePickerFragment();
        datePickerFrag.setDatePickerFragmentListener(this);
        datePickerFrag.show(getFragmentManager(), "Date Picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        patient.setDayBirth(dayOfMonth);
        patient.setMonthBirth(month + 1);
        patient.setYearBirth(year);
        tvBirthdayReg.setText(String.format("%d.%d.%d", dayOfMonth, month + 1, year));
    }

    private void checkPatient(String name, String lastname, String middlename) {
        hospitalApi.getMetadata(name, lastname, middlename, patient.getInsuranceSeries(), patient.getInsuranceNumber(),
                patient.getBirthdayFormatted(), hospitalId, "").enqueue(new Callback<CheckPatientApiResponse>() {
            @Override
            public void onResponse(Call<CheckPatientApiResponse> call, Response<CheckPatientApiResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getSuccess()) {
                        CheckPatientApiResponse respObj = response.body();
                        patient.setServiceId(respObj.getResponse().getPatientId());
                        //dataHashMap.put("patient_id", respObj.getResponse().getPatientId());

                        String cookie = response.headers().get("Set-Cookie");
                        Log.d(LOG_TAG, "patientId: " + patient.getServiceId() + ", cookie: " + cookie);

                        /*editor = mSettings.edit();
                        editor.putString(MainActivity.APP_SETTINGS_PATIENT_ID, patient.getId());
                        editor.putBoolean(MainActivity.APP_SETTINGS_USER_LOGGED_IN, true);
                        editor.apply();*/

                        /*String welcomePhr = "Добро пожаловать, " + patient.getName().substring(0,1).toUpperCase() + patient.getName().substring(1) + "!";
                        Toast.makeText(getContext(), welcomePhr, Toast.LENGTH_LONG).show();*/

                        /*Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/

                        /*editor = mSettings.edit();
                        editor.putString(MainActivity.APP_SETTINGS_PATIENT_NAME, patient.getName());
                        editor.putString(MainActivity.APP_SETTINGS_PATIENT_LASTNAME, patient.getLastName());
                        editor.putString(MainActivity.APP_SETTINGS_PATIENT_BIRTHDAY, patient.getBirthdayFormatted());
                        editor.apply();*/
                        mListener.onInputBioFragmentDataListener(patient);
                        Log.d(LOG_TAG, "Пациент найден");
                    }
                    else {
                        Log.d(LOG_TAG, "Ошибка авторизации: " + response.body().getError().getErrorDescription());
                        Toast.makeText(getContext(), "Ошибка авторизации: " + response.body().getError().getErrorDescription(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(LOG_TAG, "Запрос не прошел (" + response.code() + ")");
                    Toast.makeText(getContext(), "Запрос не прошел (\" + response.code() + \")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckPatientApiResponse> call, Throwable t) {
                Log.d(LOG_TAG, "Error: " + t.getMessage());
                String errorMessage;
                if (t.getMessage() == null) {
                    errorMessage = "Удаленный сервер недоступен";
                } else errorMessage = "Error: " + t.getMessage();
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}