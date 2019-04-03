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

    TextView tvFioProfile, tvDistrictProfile, tvHospitalProfile;

    public static ProfileFragment newInstance(Patient patient) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("name", patient.getName());
        args.putString("lastname", patient.getLastName());
        // TODO добавить остальные поля
        profileFragment.setArguments(args);
        return profileFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        tvFioProfile = v.findViewById(R.id.tvFioProfile);
        tvDistrictProfile = v.findViewById(R.id.tvDistrictProfile);
        tvHospitalProfile = v.findViewById(R.id.tvHospitalProfile);

        tvFioProfile.setText(getArguments().getString("name"));
        return v;
    }
}
