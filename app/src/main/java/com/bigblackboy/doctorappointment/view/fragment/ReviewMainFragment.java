package com.bigblackboy.doctorappointment.view.fragment;

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
import com.bigblackboy.doctorappointment.model.SharedPreferencesManager;
import com.bigblackboy.doctorappointment.presenter.ReviewMainFragmentPresenter;

public class ReviewMainFragment extends Fragment implements View.OnClickListener {

    private TextView tvHospitalReviewMain;
    private Button btnMyDoctorReviewMain, btnChangeHospitalReviewMain;
    private SharedPreferences mSettings;
    private OnReviewMainFragmentDataListener mListener;
    private ReviewMainFragmentPresenter presenter;

    public interface OnReviewMainFragmentDataListener {
        void onReviewMainFragmentBtnClick(View v);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnReviewMainFragmentDataListener) {
            mListener = (OnReviewMainFragmentDataListener) context;
        } else throw new RuntimeException(context.toString() + " must implement OnReviewMainFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettings = getActivity().getSharedPreferences(SharedPreferencesManager.APP_SETTINGS, Context.MODE_PRIVATE);
        presenter = new ReviewMainFragmentPresenter(mSettings);
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_review_main, container, false);
        tvHospitalReviewMain = v.findViewById(R.id.tvHospitalReviewMain);
        btnMyDoctorReviewMain = v.findViewById(R.id.btnMyDoctorReviewMain);
        btnMyDoctorReviewMain.setOnClickListener(this);
        btnChangeHospitalReviewMain = v.findViewById(R.id.btnChangeHospitalReviewMain);
        btnChangeHospitalReviewMain.setOnClickListener(this);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.viewIsReady();
    }

    public void setHospitalAddress(String address) {
        tvHospitalReviewMain.setText(address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMyDoctorReviewMain:
                mListener.onReviewMainFragmentBtnClick(v);
                break;
            case R.id.btnChangeHospitalReviewMain:
                mListener.onReviewMainFragmentBtnClick(v);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
