package com.bigblackboy.doctorappointment.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.activity.OnDataPass;

import java.util.HashMap;

public class InputBioFragment extends Fragment implements View.OnClickListener {

    EditText etNameReg, etLastnameReg, etBirthdayReg;
    Button btnBioReg;
    OnDataPass mDataPasser;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDataPasser = (OnDataPass) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input_bio, container, false);
        etNameReg = v.findViewById(R.id.etNameReg);
        etLastnameReg = v.findViewById(R.id.etLastnameReg);
        etBirthdayReg = v.findViewById(R.id.etBirthdayReg);
        btnBioReg = v.findViewById(R.id.btnBioReg);
        btnBioReg.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBioReg:
                if(!(TextUtils.isEmpty(etNameReg.getText().toString())) && !(TextUtils.isEmpty(etLastnameReg.getText().toString()))
                        && !(TextUtils.isEmpty(etBirthdayReg.getText().toString()))) {
                    Toast.makeText(getContext(), "Передача данных пациента", Toast.LENGTH_SHORT).show();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("name", etNameReg.getText().toString());
                    hashMap.put("lastname", etLastnameReg.getText().toString());
                    hashMap.put("birthday", etBirthdayReg.getText().toString());
                    mDataPasser.onDataPass(2, hashMap);
                } else Toast.makeText(getContext(), "Введите данные!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
