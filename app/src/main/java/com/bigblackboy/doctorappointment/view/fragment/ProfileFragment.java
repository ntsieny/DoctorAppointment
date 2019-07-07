package com.bigblackboy.doctorappointment.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.presenter.ProfileFragmentPresenter;
import com.bigblackboy.doctorappointment.view.activity.MainActivity;

public class ProfileFragment extends Fragment implements MVPBaseInterface.View, View.OnClickListener {

    private TextView tvFioProfile, tvBirthdayProfile, tvDistrictProfile, tvHospitalProfile;
    private Button btnMyAppointments, btnMyReviews, btnMyComments;
    private OnProfileFragmentDataListener mListener;
    private Patient patient;
    private ProfileFragmentPresenter presenter;

    public interface OnProfileFragmentDataListener {
        void onProfileFragmentBtnClick(View v);
    }

    public static ProfileFragment newInstance(Patient patient) {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("patient", patient);
        profileFragment.setArguments(args);
        return profileFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileFragmentDataListener) {
            mListener = (OnProfileFragmentDataListener) context;
        } else throw new RuntimeException(context.toString() + " must implement OnProfileFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ProfileFragmentPresenter();
        presenter.attachView(this);
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

        if (getArguments() != null && getArguments().containsKey("patient"))
            this.patient = (Patient) getArguments().getSerializable("patient");
        presenter.setPatientInfo(patient);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.viewIsReady();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.action_bar_title_my_profile);
    }

    public void setPatientFio(String fio) {
        tvFioProfile.setText(fio);
    }

    public void setPatientBirthday(String birthday) {
        tvBirthdayProfile.setText(birthday);
    }

    public void hideBirthdayLabel() {
        tvBirthdayProfile.setVisibility(View.GONE);
    }

    public void setDistrict(String districtName) {
        tvDistrictProfile.setText(districtName);
    }

    public void setHospital(String hospitalName) {
        tvHospitalProfile.setText(hospitalName);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMyAppointments:
                mListener.onProfileFragmentBtnClick(v);
                break;
            case R.id.btnMyReviews:
                mListener.onProfileFragmentBtnClick(v);
                break;
            case R.id.btnMyComments:
                mListener.onProfileFragmentBtnClick(v);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
