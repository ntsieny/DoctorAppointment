package com.bigblackboy.doctorappointment.view.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.presenter.InputBioFragmentPresenter;

public class InputBioFragment extends Fragment implements MVPBaseInterface.View, View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private static final String LOG_TAG = "myLog: InputBioFragment";
    private InputBioFragmentPresenter presenter;
    private EditText etNameReg, etLastnameReg, etMiddlenameReg;
    private TextView tvBirthdayReg;
    private Button btnBioReg;
    private OnInputBioFragmentDataListener mListener;
    private Patient patient;
    private String hospitalId;
    private boolean birthdayIsSet;

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
        patient = new Patient();
        patient.setHospital(new Hospital(Integer.valueOf(hospitalId), null));
        presenter = new InputBioFragmentPresenter();
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input_bio, container, false);
        etNameReg = v.findViewById(R.id.etNameReg);
        etLastnameReg = v.findViewById(R.id.etLastnameReg);
        etMiddlenameReg = v.findViewById(R.id.etMiddlenameReg);
        tvBirthdayReg = v.findViewById(R.id.tvBirthdayReg);
        tvBirthdayReg.setOnClickListener(this);
        btnBioReg = v.findViewById(R.id.btnBioReg);
        btnBioReg.setOnClickListener(this);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.viewIsReady();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBioReg:
                if(!(TextUtils.isEmpty(etNameReg.getText().toString())) && !(TextUtils.isEmpty(etLastnameReg.getText().toString())) && birthdayIsSet) {
                    patient.setName(etNameReg.getText().toString());
                    patient.setLastName(etLastnameReg.getText().toString());
                    patient.setMiddleName(etMiddlenameReg.getText().toString());
                    presenter.checkUserExistsInHospital(patient, null);
                } else Toast.makeText(getContext(), "Введите данные!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvBirthdayReg:
                presenter.onChooseDateLabelClick();
                break;
        }
    }

    public void showDatePicker() {
        DatePickerFragment datePickerFrag;
        if (patient.getDayBirth() != 0 && patient.getMonthBirth() != 0 && patient.getYearBirth() != 0) {
            datePickerFrag = DatePickerFragment.newInstance(patient.getYearBirth(), patient.getMonthBirth() - 1, patient.getDayBirth());
        } else datePickerFrag = new DatePickerFragment();
        datePickerFrag.setDatePickerFragmentListener(this);
        datePickerFrag.show(getFragmentManager(), "Date Picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        birthdayIsSet = true;
        patient.setDayBirth(dayOfMonth);
        patient.setMonthBirth(month + 1);
        patient.setYearBirth(year);
        tvBirthdayReg.setText(String.format("%d.%d.%d", dayOfMonth, month + 1, year));
    }

    public void openMainMenuActivity(Patient patient) {
        mListener.onInputBioFragmentDataListener(patient);
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
