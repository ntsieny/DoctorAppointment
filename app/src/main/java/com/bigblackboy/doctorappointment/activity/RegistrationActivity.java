package com.bigblackboy.doctorappointment.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.fragment.InputBioFragment;
import com.bigblackboy.doctorappointment.fragment.SignUpFragment;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity implements OnDataPass {

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
    public void onDataPass(int fragmentId, HashMap<String, String> data) {
        switch (fragmentId) {
            case 1:
                String login = data.get("login");
                String password = data.get("password");
                Toast.makeText(this, login + " " + password, Toast.LENGTH_SHORT).show();
                InputBioFragment inputBioFragment = new InputBioFragment();
                fm.beginTransaction().replace(R.id.linLayoutRegistration, inputBioFragment).commit();
                break;
            case 2:
                String name = data.get("name");
                String lastname = data.get("lastname");
                String birthday = data.get("birthday");
                Toast.makeText(this, name + " " + lastname + " " + birthday, Toast.LENGTH_SHORT).show();
                DistrictFragment districtFragment = new DistrictFragment();
                fm.beginTransaction().replace(R.id.linLayoutRegistration, districtFragment).commit();
                break;
            case 3:
                // получаем район от DistrictFrament
                break;
        }
    }
}
