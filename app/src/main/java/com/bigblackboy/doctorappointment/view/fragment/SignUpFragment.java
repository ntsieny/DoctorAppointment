package com.bigblackboy.doctorappointment.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.presenter.SignUpFragmentPresenter;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment implements MVPBaseInterface.View, View.OnClickListener {

    private static final String LOG_TAG = "myLog: SignUpFragment";
    private EditText etLoginReg, etPasswordReg, etPasswordRepeat;
    private Button btnSignup;
    private OnSignUpFragmentDataListener mListener;
    private TextInputLayout loginTextInputLayout;
    private SignUpFragmentPresenter presenter;

    public interface OnSignUpFragmentDataListener {
        void onSignUpFragmentDataListener(Map<String, String> loginAndPassword);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnSignUpFragmentDataListener) {
            mListener = (OnSignUpFragmentDataListener) context;
        } else throw new RuntimeException(context.toString() + " must implement onSignUpFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SignUpFragmentPresenter();
        presenter.attachView(this);
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
                presenter.checkLoginUnique(login, "");
            }
        });
        etPasswordReg = view.findViewById(R.id.etPasswordReg);
        etPasswordRepeat = view.findViewById(R.id.etPasswordRepeat);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.viewIsReady();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup:
                presenter.onBtnSignupClick();
                break;
        }
    }

    public void setInputError(String errorMessage) {
        loginTextInputLayout.setError(errorMessage);
    }

    public String getLoginInput() {
        return etLoginReg.getText().toString();
    }

    public String getPasswordInput() {
        return etPasswordReg.getText().toString();
    }

    public String getPasswordRepeatInput() {
        return etPasswordRepeat.getText().toString();
    }

    public void sendDataToActivity(HashMap<String, String> hashMap) {
        mListener.onSignUpFragmentDataListener(hashMap);
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
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
