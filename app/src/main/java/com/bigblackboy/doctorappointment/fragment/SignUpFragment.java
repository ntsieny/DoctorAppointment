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

public class SignUpFragment extends Fragment implements View.OnClickListener {

    OnDataPass mDataPasser;
    EditText etLoginReg, etPasswordReg, etPasswordRepeat;
    Button btnSignup;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDataPasser = (OnDataPass) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        btnSignup = view.findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(this);
        etLoginReg = view.findViewById(R.id.etLoginReg);
        etPasswordReg = view.findViewById(R.id.etPasswordReg);
        etPasswordRepeat = view.findViewById(R.id.etPasswordRepeat);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup:
                if(!(TextUtils.isEmpty(etLoginReg.getText().toString())) && !(TextUtils.isEmpty(etPasswordReg.getText().toString())
                        && !(TextUtils.isEmpty(etPasswordRepeat.getText().toString())))) {
                    // проверка логина на уникальность
                    // ...
                    if(etPasswordReg.getText().toString().equals(etPasswordRepeat.getText().toString())) {
                        // создание пользователя и передача в ActivityRegistration
                        HashMap<String, String> hashMap = new HashMap();
                        hashMap.put("login", etLoginReg.getText().toString());
                        hashMap.put("password", etPasswordReg.getText().toString());
                        mDataPasser.onDataPass(1, hashMap);
                    } else Toast.makeText(getContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getContext(), "Введите данные!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
