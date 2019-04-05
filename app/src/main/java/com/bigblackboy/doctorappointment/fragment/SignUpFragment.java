package com.bigblackboy.doctorappointment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.SpringApi;
import com.bigblackboy.doctorappointment.SpringController;
import com.bigblackboy.doctorappointment.springserver.Response;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = "myLog: SignUpFragment";
    EditText etLoginReg, etPasswordReg, etPasswordRepeat;
    Button btnSignup;
    ImageView ivCheckLogin;
    OnSignUpFragmentDataListener mListener;
    private SpringApi springApi;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnSignUpFragmentDataListener) {
            mListener = (OnSignUpFragmentDataListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement onSignUpFragmentDataListener");
        }
    }

    public interface OnSignUpFragmentDataListener {
        void onSignUpFragmentDataListener(Map<String, String> loginAndPassword);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        btnSignup = view.findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(this);
        ivCheckLogin = view.findViewById(R.id.ivCheckLogin);
        etLoginReg = view.findViewById(R.id.etLoginReg);
        etLoginReg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v == etLoginReg && !hasFocus) {
                    checkLoginUnique(etLoginReg.getText().toString());
                    ivCheckLogin.setVisibility(View.VISIBLE);
                }
            }
        });
        etPasswordReg = view.findViewById(R.id.etPasswordReg);
        etPasswordRepeat = view.findViewById(R.id.etPasswordRepeat);
        springApi = SpringController.getApi();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup:
                if(!(TextUtils.isEmpty(etLoginReg.getText().toString())) && !(TextUtils.isEmpty(etPasswordReg.getText().toString())
                        && !(TextUtils.isEmpty(etPasswordRepeat.getText().toString())))) {
                    if(etPasswordReg.getText().toString().equals(etPasswordRepeat.getText().toString())) {
                        // создание пользователя и передача в ActivityRegistration
                        HashMap<String, String> hashMap = new HashMap();
                        hashMap.put("login", etLoginReg.getText().toString());
                        hashMap.put("password", etPasswordReg.getText().toString());
                        mListener.onSignUpFragmentDataListener(hashMap);
                    } else Toast.makeText(getContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getContext(), "Введите данные!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void checkLoginUnique(String login) {
        springApi.checkLoginUnique(login).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response resp = response.body();
                if (resp.isSuccess()) {
                    Log.d(LOG_TAG, "Логин свободен!");
                    Toast.makeText(getContext(), "Логин свободен", Toast.LENGTH_SHORT).show();
                    ivCheckLogin.setImageDrawable(getResources().getDrawable(R.drawable.checked));
                }
                else {
                    Log.d(LOG_TAG, "Такой логин уже занят!");
                    Toast.makeText(getContext(), "Логин занят", Toast.LENGTH_SHORT).show();
                    ivCheckLogin.setImageDrawable(getResources().getDrawable(R.drawable.not_checked));
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                ivCheckLogin.setVisibility(View.INVISIBLE);
            }
        });
    }
}
