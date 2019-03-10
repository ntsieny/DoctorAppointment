package com.bigblackboy.doctorappointment.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.activity.OnDataPass;
import com.bigblackboy.doctorappointment.model.Patient;

import java.util.Calendar;
import java.util.HashMap;

public class LoginFragment extends Fragment implements View.OnClickListener {

    EditText etLastname, etName;
    TextView tvBirthday;
    Button btnLogin;
    OnDataPass mDataPasser;
    Patient patient;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDataPasser = (OnDataPass) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        patient = new Patient();
        etLastname = getView().findViewById(R.id.etLastname);
        etName = getView().findViewById(R.id.etName);
        tvBirthday = getView().findViewById(R.id.tvBirthday);
        tvBirthday.setOnClickListener(this);

        btnLogin = getView().findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", etName.getText().toString());
                hashMap.put("lastname", etLastname.getText().toString());
                hashMap.put("daybirth", String.valueOf(patient.getDayBirth()));
                hashMap.put("monthbirth", String.valueOf(patient.getMonthBirth()));
                hashMap.put("yearbirth", String.valueOf(patient.getYearBirth()));
                mDataPasser.onDataPass(3, hashMap);
                break;
            case R.id.tvBirthday:
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
            tvBirthday.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year));
        }
    };
}
