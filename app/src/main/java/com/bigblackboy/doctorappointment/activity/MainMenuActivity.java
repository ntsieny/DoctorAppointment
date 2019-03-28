package com.bigblackboy.doctorappointment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.fragment.AppointmentFragment;
import com.bigblackboy.doctorappointment.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.fragment.DoctorFragment;
import com.bigblackboy.doctorappointment.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.fragment.MainMenuFragment;
import com.bigblackboy.doctorappointment.fragment.SpecialityFragment;
import com.bigblackboy.doctorappointment.model.District;
import com.bigblackboy.doctorappointment.model.Hospital;

import java.util.HashMap;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener, OnDataPass, DistrictFragment.OnDistrictFragmentDataListener,
        HospitalFragment.OnHospitalFragmentDataListener {

    public static final String APP_SETTINGS = "app_settings";
    public static final String APP_SETTINGS_USER_LOGGED_IN = "user_logged_in";
    public static final String APP_SETTINGS_DISTRICT_ID = "district_id";
    public static final String APP_SETTINGS_HOSPITAL_ID = "hospital_id";
    public static final String APP_SETTINGS_PATIENT_ID = "patient_id";
    public static final String APP_SETTINGS_PATIENT_NAME = "patient_name";
    public static final String APP_SETTINGS_PATIENT_LASTNAME = "patient_lastname";
    public static final String APP_SETTINGS_PATIENT_BIRTHDAY = "patient_birthday";
    SharedPreferences mSettings;
    FragmentManager fm;
    FragmentTransaction fTrans;
    Fragment districtFragment;
    NavigationView navigationView;
    private District district;
    //public DistrictToFragment districtToFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mSettings = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        fm = getSupportFragmentManager();

        if (mSettings.getBoolean(MainMenuActivity.APP_SETTINGS_USER_LOGGED_IN, false)) {
            MainMenuFragment mainMenuFragment = new MainMenuFragment();
            fm.beginTransaction().add(R.id.fragContainer, mainMenuFragment).commit();

            setNavigationDrawer();

            String lastname = mSettings.getString(MainMenuActivity.APP_SETTINGS_PATIENT_LASTNAME, "");
            String name = mSettings.getString(MainMenuActivity.APP_SETTINGS_PATIENT_NAME, "");
            String fio = lastname + " " + name;
            TextView tvPatientFIO = navigationView.getHeaderView(0).findViewById(R.id.tvPatientFIO);
            tvPatientFIO.setText(fio);
        }
        else {
            String btnAction = getIntent().getStringExtra("btn");
            if(btnAction.equals("loginUser") || btnAction.equals("registration")) {
                //открытие меню пользователя
                Toast.makeText(this, btnAction + " Здесь будет инфо о пользователе/меню", Toast.LENGTH_SHORT).show();
                setNavigationDrawer();
            }
            else if(btnAction.equals("loginGuest")) {
                // создание фрагмента выбора района и города для гостя
                DistrictFragment districtFragment = new DistrictFragment();
                fm.beginTransaction().add(R.id.fragContainer, districtFragment).commit();
                setNavigationDrawer();
            }
        }
    }

    private void setNavigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.logoutItem:
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean(APP_SETTINGS_USER_LOGGED_IN, false);
                editor.apply();

                intent = new Intent(this, ChooseLoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.btnMakeAppointment:
                fm = getSupportFragmentManager();
                districtFragment = new DistrictFragment();
                fm.beginTransaction().replace(R.id.linLayoutMainMenu, districtFragment).commit();
                break;
        }*/
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDataPass(int fragmentId, HashMap hashMap) {
        Bundle bundle;
        switch (fragmentId) {
            case 2:
                fTrans = fm.beginTransaction();
                SpecialityFragment specFragment = new SpecialityFragment();

                bundle = new Bundle();
                bundle.putSerializable("hashmap", hashMap);
                specFragment.setArguments(bundle);
                fTrans.replace(R.id.fragContainer, specFragment).addToBackStack("fragment_hospital");
                fTrans.commit();
                break;
            case 3:
                fTrans = fm.beginTransaction();
                DoctorFragment doctorFragment = new DoctorFragment();

                bundle = new Bundle();
                bundle.putSerializable("hashmap", hashMap);
                doctorFragment.setArguments(bundle);
                fTrans.replace(R.id.fragContainer, doctorFragment).addToBackStack("spec_fragment");
                fTrans.commit();
                break;
            case 4:
                fTrans = fm.beginTransaction();
                AppointmentFragment appointmentFragment = new AppointmentFragment();

                bundle = new Bundle();
                bundle.putSerializable("hashmap", hashMap);
                appointmentFragment.setArguments(bundle);
                fTrans.replace(R.id.fragContainer, appointmentFragment).addToBackStack("fragment_doctor");
                fTrans.commit();
                break;
            case 100:
                fm = getSupportFragmentManager();
                districtFragment = new DistrictFragment();
                fm.beginTransaction().replace(R.id.fragContainer, districtFragment).commit();
                break;
        }
    }

    @Override
    public void onDistrictFragmentDataListener(District district) {
        HospitalFragment hospitalFragment = new HospitalFragment();

        fTrans = fm.beginTransaction().replace(R.id.fragContainer, hospitalFragment).addToBackStack("fragment_district");
        fTrans.commit();

        hospitalFragment.setDistrict(district);
        /*if(districtToFragment != null) {
            districtToFragment.districtToFragment(district);
        } else Toast.makeText(this, "mDistrictFragment is NULL", Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onHospitalFragmentDataListener(Hospital hospital) {
        Toast.makeText(this, "Активити получила hospital " + hospital.getLPUShortName(), Toast.LENGTH_SHORT).show();
        // открытие фрагмента главного меню
    }

    /*public interface DistrictToFragment {
        void districtToFragment (District district);
    }*/
}
