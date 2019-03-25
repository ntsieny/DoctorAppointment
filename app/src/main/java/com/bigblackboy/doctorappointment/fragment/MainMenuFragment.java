package com.bigblackboy.doctorappointment.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.activity.OnDataPass;

import java.util.HashMap;

public class MainMenuFragment extends Fragment implements View.OnClickListener {

    Button btnMakeAppointment;
    OnDataPass mDataPasser;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDataPasser = (OnDataPass) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnMakeAppointment = getView().findViewById(R.id.btnMakeAppointment);
        btnMakeAppointment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMakeAppointment:
                mDataPasser.onDataPass(100, null);
                break;
        }
    }
}
