package com.bigblackboy.doctorappointment.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.Patient;

public class ProfileFragment extends Fragment {

    TextView tvFioProfile, tvBirthdayProfile, tvDistrictProfile, tvHospitalProfile;

    public static ProfileFragment newInstance(Patient patient) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("name", patient.getName());
        args.putString("lastname", patient.getLastName());
        args.putString("middlename", patient.getMiddleName());
        args.putInt("dayBirth", patient.getDayBirth());
        args.putInt("monthBirth", patient.getMonthBirth());
        args.putInt("yearBirth", patient.getYearBirth());
        args.putString("districtName", patient.getDistrict().getName());
        args.putString("hospitalNameShort", patient.getHospital().getLPUShortName());
        profileFragment.setArguments(args);
        return profileFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        tvFioProfile = v.findViewById(R.id.tvFioProfile);
        tvBirthdayProfile = v.findViewById(R.id.tvBirthdayProfile);
        tvDistrictProfile = v.findViewById(R.id.tvDistrictProfile);
        tvHospitalProfile = v.findViewById(R.id.tvHospitalProfile);

        String name = getArguments().getString("name");
        String lastname = getArguments().getString("lastname");
        String middlename = getArguments().getString("middlename");
        tvFioProfile.setText(String.format("%s %s %s", lastname, name, middlename));

        int dayBirth = getArguments().getInt("dayBirth");
        int monthBirth = getArguments().getInt("monthBirth");
        int yearBirth = getArguments().getInt("yearBirth");
        String birthday = String.format("%d.%d.%d", dayBirth, monthBirth, yearBirth);
        tvBirthdayProfile.setText(birthday);

        tvDistrictProfile.setText(getArguments().getString("districtName"));
        tvHospitalProfile.setText(getArguments().getString("hospitalNameShort"));
        return v;
    }
}
