package com.bigblackboy.doctorappointment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.fragment.AppointmentFragment;
import com.bigblackboy.doctorappointment.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.fragment.DoctorFragment;
import com.bigblackboy.doctorappointment.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.fragment.MainMenuFragment;
import com.bigblackboy.doctorappointment.fragment.ProfileFragment;
import com.bigblackboy.doctorappointment.fragment.SpecialityFragment;
import com.bigblackboy.doctorappointment.model.AppointmentInfo;
import com.bigblackboy.doctorappointment.model.District;
import com.bigblackboy.doctorappointment.model.Doctor;
import com.bigblackboy.doctorappointment.model.Hospital;
import com.bigblackboy.doctorappointment.model.Patient;
import com.bigblackboy.doctorappointment.model.Speciality;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DistrictFragment.OnDistrictFragmentDataListener,
        HospitalFragment.OnHospitalFragmentDataListener, SpecialityFragment.OnSpecialityFragmentDataListener, DoctorFragment.OnDoctorFragmentDataListener,
        AppointmentFragment.OnAppointmentFragmentDataListener, MainMenuFragment.OnMainMenuFragmentDataListener {

    public static final String APP_SETTINGS = "app_settings";
    public static final String APP_SETTINGS_USER_LOGGED_IN = "user_logged_in";
    public static final String APP_SETTINGS_GUEST_MODE = "guest_mode";
    public static final String APP_SETTINGS_DISTRICT_ID = "district_id";
    public static final String APP_SETTINGS_DISTRICT_NAME = "district_name";
    public static final String APP_SETTINGS_HOSPITAL_ID = "hospital_id";
    public static final String APP_SETTINGS_HOSPITAL_NAME_SHORT = "hospital_name_short";
    public static final String APP_SETTINGS_HOSPITAL_NAME_FULL = "hospital_name_full";
    public static final String APP_SETTINGS_PATIENT_ID = "patient_id";
    public static final String APP_SETTINGS_PATIENT_NAME = "patient_name";
    public static final String APP_SETTINGS_PATIENT_LASTNAME = "patient_lastname";
    public static final String APP_SETTINGS_PATIENT_DAYBIRTH = "patient_daybirth";
    public static final String APP_SETTINGS_PATIENT_MONTHBIRTH = "patient_monthbirth";
    public static final String APP_SETTINGS_PATIENT_YEARBIRTH = "patient_yearbirth";
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    FragmentManager fm;
    FragmentTransaction fTrans;
    NavigationView navigationView;
    private String patientId;
    private Patient patient;
    private District district;
    private Hospital hospital;
    private Speciality speciality;
    private Doctor doctor;
    private boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mSettings = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        fm = getSupportFragmentManager();
        // TODO получить patient из Bundle/или не получать, а считывать данные из SharedPreferences
        patient = new Patient();
        district = new District();
        hospital = new Hospital();
        speciality = new Speciality();
        doctor = new Doctor();
        String hospitalName = mSettings.getString(MainMenuActivity.APP_SETTINGS_HOSPITAL_NAME_SHORT, "");
        String districtName = mSettings.getString(MainMenuActivity.APP_SETTINGS_DISTRICT_NAME, "");
        boolean userLoggedIn = mSettings.getBoolean(MainMenuActivity.APP_SETTINGS_USER_LOGGED_IN, false);
        boolean guestMode = mSettings.getBoolean(MainMenuActivity.APP_SETTINGS_GUEST_MODE, false);

        if (userLoggedIn) {
            loggedIn = true;
            MainMenuFragment mainMenuFragment = new MainMenuFragment();
            fm.beginTransaction().add(R.id.fragContainer, mainMenuFragment).commit();
            setNavigationDrawer();

            String lastname = mSettings.getString(MainMenuActivity.APP_SETTINGS_PATIENT_LASTNAME, "");
            patient.setLastName(lastname);
            String name = mSettings.getString(MainMenuActivity.APP_SETTINGS_PATIENT_NAME, "");
            patient.setName(name);
            String fio = lastname + " " + name;
            TextView tvPatientFIO = navigationView.getHeaderView(0).findViewById(R.id.tvPatientFIO);
            tvPatientFIO.setText(fio);

            TextView tvPatientAddress = navigationView.getHeaderView(0).findViewById(R.id.tvPatientAddress);
            tvPatientAddress.setText(hospitalName + ", " + districtName);

            String hospitalId = mSettings.getString(MainMenuActivity.APP_SETTINGS_HOSPITAL_ID, null);
            hospital.setIdLPU(Integer.parseInt(hospitalId));
            patientId = mSettings.getString(MainMenuActivity.APP_SETTINGS_PATIENT_ID, null);
        }
        else if (guestMode) {
            MainMenuFragment mainMenuFragment = new MainMenuFragment();
            fm.beginTransaction().add(R.id.fragContainer, mainMenuFragment).commit();
            setNavigationDrawer();

            patient.setName("Гость");

            TextView tvPatientFIO = navigationView.getHeaderView(0).findViewById(R.id.tvPatientFIO);
            tvPatientFIO.setText(patient.getName());

            TextView tvPatientAddress = navigationView.getHeaderView(0).findViewById(R.id.tvPatientAddress);
            tvPatientAddress.setText(hospitalName + ", " + districtName);
        }
        else {
            DistrictFragment districtFragment = new DistrictFragment();
            fm.beginTransaction().add(R.id.fragContainer, districtFragment).commit();
            setNavigationDrawer();
        }
    }

    private void setNavigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
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
                clearSettings();
                finish();
                intent = new Intent(this, ChooseLoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearSettings() {
        editor = mSettings.edit();
        editor.clear().apply();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.main_menu:
                showMainMenuFragment();
                break;
            case R.id.profile:
                ProfileFragment profileFragment = ProfileFragment.newInstance(patient);
                fm.beginTransaction().replace(R.id.fragContainer, profileFragment).commit();
                break;
            case R.id.make_appointment:
                if(loggedIn)
                    showSpecialityFragment();
                else Toast.makeText(this, "Необходимо войти в аккаунт", Toast.LENGTH_SHORT).show();
                break;
            case R.id.doctor_schedule:

                break;
            case R.id.appointment_history:
                if (loggedIn) {

                } else Toast.makeText(this, "Необходимо войти в аккаунт", Toast.LENGTH_SHORT).show();
                break;
            case R.id.area_number:

                break;
            case R.id.reviews:

                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        int backstackCount = fm.getBackStackEntryCount();
        if (backstackCount == 0) {
            super.onBackPressed();
        } else fm.popBackStack();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void showMainMenuFragment() {
        MainMenuFragment mainMenuFragment = new MainMenuFragment();
        fm.beginTransaction().replace(R.id.fragContainer, mainMenuFragment).commit();
    }

    private void showSpecialityFragment() {
        String hospitalId = String.valueOf(hospital.getIdLPU());
        SpecialityFragment specialityFragment = SpecialityFragment.newInstance(hospitalId, patientId);
        fm.beginTransaction().replace(R.id.fragContainer, specialityFragment).commit();
    }

    @Override
    public void onDistrictFragmentDataListener(District district) {
        this.district = district;
        HospitalFragment hospitalFragment = HospitalFragment.newInstance(district);
        fTrans = fm.beginTransaction().replace(R.id.fragContainer, hospitalFragment).addToBackStack("fragment_district");
        fTrans.commit();
    }

    @Override
    public void onDistrictUpdateActionBarTitle(String barTitle) {
        getSupportActionBar().setTitle(barTitle);
    }

    @Override
    public void onHospitalFragmentDataListener(Hospital hospital) {
        this.hospital = hospital;
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        MainMenuFragment mainMenuFragment = new MainMenuFragment();
        fm.beginTransaction().replace(R.id.fragContainer, mainMenuFragment).commit();

        if(!loggedIn) {
            editor = mSettings.edit();
            editor.putBoolean(MainMenuActivity.APP_SETTINGS_GUEST_MODE, true);
            editor.putString(MainMenuActivity.APP_SETTINGS_DISTRICT_NAME, district.getName());
            editor.putString(MainMenuActivity.APP_SETTINGS_HOSPITAL_NAME_SHORT, hospital.getLPUShortName());
            editor.apply();
        }

        TextView tvPatientFIO = navigationView.getHeaderView(0).findViewById(R.id.tvPatientFIO);
        tvPatientFIO.setText("Гость");
        TextView tvPatientAddress = navigationView.getHeaderView(0).findViewById(R.id.tvPatientAddress);
        tvPatientAddress.setText(hospital.getLPUShortName() + ", " + district.getName());
    }

    @Override
    public void onHospitalUpdateActionBarTitle(String barTitle) {
        getSupportActionBar().setTitle(barTitle);
    }

    @Override
    public void onSpecialityFragmentDataListener(Speciality speciality) {
        this.speciality = speciality;
        String hospitalId = String.valueOf(hospital.getIdLPU());
        String specialityId = speciality.getIdSpeciality();
        // TODO создать Patient для работы с пациентом, убрать поле patientID
        DoctorFragment doctorFragment = DoctorFragment.newInstance(hospitalId, patientId, specialityId);
        fm.beginTransaction().replace(R.id.fragContainer, doctorFragment).addToBackStack("spec_fragment").commit();
    }

    @Override
    public void onSpecialityUpdateActionBarTitle(String barTitle) {
        getSupportActionBar().setTitle(barTitle);
    }

    @Override
    public void onDoctorFragmentDataListener(Doctor doctor) {
        this.doctor = doctor;
        String doctorId = doctor.getIdDoc();
        String hospitalId = String.valueOf(hospital.getIdLPU());
        // TODO взять patientID из объекта Patient
        AppointmentFragment appointmentFragment = AppointmentFragment.newInstance(doctorId, hospitalId, patientId);
        fm.beginTransaction().replace(R.id.fragContainer, appointmentFragment).addToBackStack("doctor_fragment").commit();
    }

    @Override
    public void onDoctorUpdateActionBarTitle(String barTitle) {
        getSupportActionBar().setTitle(barTitle);
    }

    @Override
    public void onAppointmentFragmentDataListener(AppointmentInfo appointmentInfo) {
        Toast.makeText(this, appointmentInfo.getDateStart().getDateTime(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAppointmentUpdateActionBarTitle(String barTitle) {
        getSupportActionBar().setTitle(barTitle);
    }

    @Override
    public void onMainMenuFragmentBtnClicked(int btnId) {
        switch (btnId) {
            case R.id.btnMakeAppointment:
                if(loggedIn)
                    showSpecialityFragment();
                else Toast.makeText(this, "Необходимо войти в аккаунт", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onMainMenuUpdateActionBarTitle(String barTitle) {
        getSupportActionBar().setTitle(barTitle);
    }
}
