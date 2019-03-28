package com.bigblackboy.doctorappointment.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.fragment.InputBioFragment;
import com.bigblackboy.doctorappointment.fragment.SignUpFragment;
import com.bigblackboy.doctorappointment.model.District;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements DistrictFragment.OnDistrictFragmentDataListener,
        SignUpFragment.OnSignUpFragmentDataListener, InputBioFragment.OnInputBioFragmentDataListener {

    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fm = getSupportFragmentManager();
        Fragment signUpFragment = new SignUpFragment();
        fm.beginTransaction().add(R.id.linLayoutRegistration, signUpFragment).commit();
    }

    @Override
    public void onDistrictFragmentDataListener(District district) {
        Toast.makeText(this, district.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("btn", "registration");
        startActivity(intent);
    }

    @Override
    public void onSignUpFragmentDataListener(Map<String, String> loginAndPassword) {
        String login = loginAndPassword.get("login");
        String password = loginAndPassword.get("password");
        InputBioFragment inputBioFragment = new InputBioFragment();
        fm.beginTransaction().replace(R.id.linLayoutRegistration, inputBioFragment).commit();
        Toast.makeText(this, login + " " + password, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInputBioFragmentDataListener(Map<String, String> bioData) {
        String name = bioData.get("name");
        String lastname = bioData.get("lastname");
        String birthday = bioData.get("birthday");
        Toast.makeText(this, name + " " + lastname + " " + birthday, Toast.LENGTH_SHORT).show();
        DistrictFragment districtFragment = new DistrictFragment();
        fm.beginTransaction().replace(R.id.linLayoutRegistration, districtFragment).commit();
    }
}
