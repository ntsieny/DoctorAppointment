package com.bigblackboy.doctorappointment.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.SharedPreferencesManager;
import com.bigblackboy.doctorappointment.activity.ReviewActivity;
import com.bigblackboy.doctorappointment.model.Patient;

public class ReviewMainFragment extends Fragment implements View.OnClickListener {

    TextView tvHospitalReviewMain;
    Button btnMyDoctorReviewMain, btnChangeHospitalReviewMain;
    SharedPreferences mSettings;
    SharedPreferencesManager prefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSettings = this.getActivity().getSharedPreferences(SharedPreferencesManager.APP_SETTINGS, Context.MODE_PRIVATE);
        prefManager = new SharedPreferencesManager(mSettings);

        View v = inflater.inflate(R.layout.fragment_review_main, container, false);
        tvHospitalReviewMain = v.findViewById(R.id.tvHospitalReviewMain);
        tvHospitalReviewMain.setText(prefManager.getCurrentHospital().getLPUShortName() + ", " + prefManager.getCurrentDistrict().getName());

        btnMyDoctorReviewMain = v.findViewById(R.id.btnMyDoctorReviewMain);
        btnMyDoctorReviewMain.setOnClickListener(this);
        btnChangeHospitalReviewMain = v.findViewById(R.id.btnChangeHospitalReviewMain);
        btnChangeHospitalReviewMain.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMyDoctorReviewMain:
                ((ReviewActivity) getActivity()).replaceToSpecialityFragment();
                break;
            case R.id.btnChangeHospitalReviewMain:

                break;
        }
    }
}
