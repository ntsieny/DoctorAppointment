package com.bigblackboy.doctorappointment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bigblackboy.doctorappointment.R;

public class MainMenuFragment extends Fragment implements View.OnClickListener {

    private Button btnMakeAppointment;
    private OnMainMenuFragmentDataListener mListener;
    private String barTitle = "Главное меню";

    public interface OnMainMenuFragmentDataListener {
        void onMainMenuFragmentBtnClicked(int btnId);

        void onMainMenuUpdateActionBarTitle(String barTitle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainMenuFragmentDataListener) {
            mListener = (OnMainMenuFragmentDataListener)context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnMainMenuFragmentDataListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mListener.onMainMenuUpdateActionBarTitle(barTitle);
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
        int btnId = v.getId();
        switch (btnId) {
            case R.id.btnMakeAppointment:
                mListener.onMainMenuFragmentBtnClicked(btnId);
                break;
        }
    }
}
