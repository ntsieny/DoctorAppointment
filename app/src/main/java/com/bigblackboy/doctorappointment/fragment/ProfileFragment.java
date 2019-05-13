package com.bigblackboy.doctorappointment.fragment;

import android.content.Intent;
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
import com.bigblackboy.doctorappointment.activity.MainMenuActivity;
import com.bigblackboy.doctorappointment.activity.ReviewActivity;
import com.bigblackboy.doctorappointment.model.Patient;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    TextView tvFioProfile, tvBirthdayProfile, tvDistrictProfile, tvHospitalProfile;
    Button btnMyAppointments, btnMyReviews, btnMyComments;


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
        btnMyAppointments = v.findViewById(R.id.btnMyAppointments);
        btnMyAppointments.setOnClickListener(this);
        btnMyReviews = v.findViewById(R.id.btnMyReviews);
        btnMyReviews.setOnClickListener(this);
        btnMyComments = v.findViewById(R.id.btnMyComments);
        btnMyComments.setOnClickListener(this);

        String name = getArguments().getString("name");
        String lastname = getArguments().getString("lastname");
        String middlename = getArguments().getString("middlename");
        if (name.equals("Гость")) {
            tvFioProfile.setText(name);
        } else {
            tvFioProfile.setText(String.format("%s %s %s", lastname, name, middlename));
        }

        int dayBirth = getArguments().getInt("dayBirth");
        int monthBirth = getArguments().getInt("monthBirth");
        int yearBirth = getArguments().getInt("yearBirth");
        String birthday = String.format("%d.%d.%d", dayBirth, monthBirth, yearBirth);
        tvBirthdayProfile.setText(birthday);

        tvDistrictProfile.setText(getArguments().getString("districtName"));
        tvHospitalProfile.setText(getArguments().getString("hospitalNameShort"));
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMyAppointments:
                ((MainMenuActivity) getActivity()).replaceToAppointmentHistoryFragment();
                break;
            case R.id.btnMyReviews:
                Intent intent = new Intent(getContext(), ReviewActivity.class);
                intent.putExtra("fragToLoad", ReviewActivity.FRAGMENT_MY_REVIEWS);
                startActivity(intent);
                break;
            case R.id.btnMyComments:

                break;
        }
    }
}
