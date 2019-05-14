package com.bigblackboy.doctorappointment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.springserver.Response;
import com.bigblackboy.doctorappointment.springserver.springmodel.User;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = "myLog: SignUpFragment";
    private EditText etLoginReg, etPasswordReg, etPasswordRepeat;
    private Button btnSignup;
    private OnSignUpFragmentDataListener mListener;
    private SpringApi springApi;
    private TextInputLayout loginTextInputLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnSignUpFragmentDataListener) {
            mListener = (OnSignUpFragmentDataListener) context;
        } else throw new RuntimeException(context.toString() + " must implement onSignUpFragmentDataListener");
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
        loginTextInputLayout = view.findViewById(R.id.loginTextInputLayout);
        etLoginReg = view.findViewById(R.id.etLoginReg);
        etLoginReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String login = etLoginReg.getText().toString();
                if (login.length() > 6) {
                    checkLoginUnique(login, "");
                } else loginTextInputLayout.setError("Логин должен быть длиннее 6 символов");

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
                String login = etLoginReg.getText().toString();
                String password = etPasswordReg.getText().toString();
                String passwordRepeat = etPasswordRepeat.getText().toString();
                if(!(TextUtils.isEmpty(login)) && !(TextUtils.isEmpty(password) && !(TextUtils.isEmpty(passwordRepeat)))) {
                    if(password.equals(passwordRepeat)) {
                        if (loginTextInputLayout.getError() == null) {
                            // создание пользователя и передача в ActivityRegistration
                            HashMap<String, String> hashMap = new HashMap();
                            hashMap.put("login", etLoginReg.getText().toString());
                            hashMap.put("password", etPasswordReg.getText().toString());
                            mListener.onSignUpFragmentDataListener(hashMap);
                        } else Toast.makeText(getContext(), "Придумайте другой логин", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getContext(), "Введите данные!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void checkLoginUnique(String login, String serviceId) {
        User user = new User();
        user.setLogin(login);
        user.setServiceId(serviceId);
        springApi.checkLoginUnique(user).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response resp = response.body();
                if (resp.isSuccess())
                    loginTextInputLayout.setError("");
                else loginTextInputLayout.setError("Логин занят");
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
