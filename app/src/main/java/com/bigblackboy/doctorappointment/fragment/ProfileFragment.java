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
import com.bigblackboy.doctorappointment.activity.MainActivity;
import com.bigblackboy.doctorappointment.activity.ReviewActivity;
import com.bigblackboy.doctorappointment.model.Patient;
import com.bigblackboy.doctorappointment.utils.AgeCalculator;

import org.joda.time.LocalDate;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    TextView tvFioProfile, tvBirthdayProfile, tvDistrictProfile, tvHospitalProfile;
    Button btnMyAppointments, btnMyReviews, btnMyComments;


    public static ProfileFragment newInstance(Patient patient) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        if(patient.getName() != null)
            args.putString("name", patient.getName());
        if(patient.getLastName() != null)
            args.putString("lastname", patient.getLastName());
        if(patient.getMiddleName() != null)
            args.putString("middlename", patient.getMiddleName());
        if(patient.getDayBirth() > 0 && patient.getDayBirth() <= 31)
            args.putInt("dayBirth", patient.getDayBirth());
        if(patient.getMonthBirth() > 0 && patient.getMonthBirth() <= 12)
            args.putInt("monthBirth", patient.getMonthBirth());
        if(patient.getYearBirth() > 0)
            args.putInt("yearBirth", patient.getYearBirth());
        if(patient.getDistrict().getName() != null)
            args.putString("districtName", patient.getDistrict().getName());
        if(patient.getHospital().getLPUShortName() != null)
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

        if (getArguments().containsKey("name") && getArguments().containsKey("lastname")) {
            String name = getArguments().getString("name");
            String lastname = getArguments().getString("lastname");
            String middlename = getArguments().getString("middlename");
            tvFioProfile.setText(String.format("%s %s %s", lastname, name, middlename));
        } else if (getArguments().containsKey("name") && !getArguments().containsKey("lastname")) {
            String name = getArguments().getString("name");
            tvFioProfile.setText(name);
        }

        if(getArguments().containsKey("dayBirth") && getArguments().containsKey("monthBirth") && getArguments().containsKey("yearBirth")) {
            int dayBirth = getArguments().getInt("dayBirth");
            int monthBirth = getArguments().getInt("monthBirth");
            int yearBirth = getArguments().getInt("yearBirth");
            String birthday = String.format("%d.%d.%d", dayBirth, monthBirth, yearBirth);
            String ageVerbal = AgeCalculator.getAgeVerbal(new LocalDate(yearBirth, monthBirth, dayBirth), new LocalDate());
            tvBirthdayProfile.setText(String.format("%s, %s", birthday, ageVerbal));
        } else tvBirthdayProfile.setVisibility(View.GONE);

        tvDistrictProfile.setText(getArguments().getString("districtName"));
        tvHospitalProfile.setText(getArguments().getString("hospitalNameShort"));
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMyAppointments:
                ((MainActivity) getActivity()).replaceToAppointmentHistoryFragment();
                break;
            case R.id.btnMyReviews:
                Intent myReviewsIntent = new Intent(getContext(), ReviewActivity.class);
                myReviewsIntent.putExtra("fragToLoad", ReviewActivity.FRAGMENT_MY_REVIEWS);
                startActivity(myReviewsIntent);
                break;
            case R.id.btnMyComments:
                Intent myCommentsIntent = new Intent(getContext(), ReviewActivity.class);
                myCommentsIntent.putExtra("fragToLoad", ReviewActivity.FRAGMENT_MY_COMMENTS);
                startActivity(myCommentsIntent);
                break;
        }
    }
}
