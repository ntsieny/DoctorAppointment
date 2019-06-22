package com.bigblackboy.doctorappointment.view.activity;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.utils.SharedPreferencesManager;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.view.fragment.AppointmentHistoryFragment;
import com.bigblackboy.doctorappointment.view.fragment.CheckupFragment;
import com.bigblackboy.doctorappointment.view.fragment.ChooseAppointmentFragment;
import com.bigblackboy.doctorappointment.view.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.view.fragment.DoctorFragment;
import com.bigblackboy.doctorappointment.view.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.view.fragment.ProfileFragment;
import com.bigblackboy.doctorappointment.view.fragment.SpecialityFragment;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.AppointmentInfo;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;
import com.bigblackboy.doctorappointment.pojos.springpojos.Response;
import com.bigblackboy.doctorappointment.pojos.springpojos.Appointment;
import com.bigblackboy.doctorappointment.utils.AgeCalculator;

import org.joda.time.LocalDate;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_DISTRICT_ID;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_DISTRICT_NAME;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_GUEST_MODE;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_ID;
import static com.bigblackboy.doctorappointment.utils.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_NAME_SHORT;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DistrictFragment.OnDistrictFragmentDataListener,
        HospitalFragment.OnHospitalFragmentDataListener, SpecialityFragment.OnSpecialityFragmentDataListener, DoctorFragment.OnDoctorFragmentDataListener,
        ChooseAppointmentFragment.OnAppointmentFragmentDataListener, ProfileFragment.OnProfileFragmentDataListener {

    private static final String LOG_TAG = "myLog: MainActivity";
    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;
    private FragmentManager fm;
    private FragmentTransaction fTrans;
    private NavigationView navigationView;
    private Patient patient;

    public Speciality getSpeciality() {
        return speciality;
    }

    private Speciality speciality;
    private Doctor doctor;
    private boolean loggedIn;
    private boolean guestMode;
    private SpringApi springApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        springApi = SpringController.getApi();

        mSettings = getSharedPreferences(SharedPreferencesManager.APP_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferencesManager prefManager = new SharedPreferencesManager(mSettings);

        fm = getSupportFragmentManager();
        patient = prefManager.getCurrentPatient();

        speciality = new Speciality();
        doctor = new Doctor();

        if (prefManager.isUserLoggedIn()) {
            loggedIn = true;
            setNavigationDrawer();
            replaceToProfileFragment();

            String fio = String.format("%s %s %s", patient.getLastName(), patient.getName(), patient.getMiddleName());
            TextView tvPatientFIO = navigationView.getHeaderView(0).findViewById(R.id.tvPatientFIO);
            tvPatientFIO.setText(fio);

            TextView tvPatientAddress = navigationView.getHeaderView(0).findViewById(R.id.tvPatientAddress);
            tvPatientAddress.setText(patient.getHospital().getLPUShortName() + ", " + patient.getDistrict().getName());
        }
        else if (prefManager.isGuestMode()) {
            guestMode = true;
            setNavigationDrawer();
            replaceToProfileFragment();

            TextView tvPatientFIO = navigationView.getHeaderView(0).findViewById(R.id.tvPatientFIO);
            tvPatientFIO.setText(patient.getName());

            TextView tvPatientAddress = navigationView.getHeaderView(0).findViewById(R.id.tvPatientAddress);
            tvPatientAddress.setText(patient.getHospital().getLPUShortName() + ", " + patient.getDistrict().getName());
        }
        else {
            DistrictFragment districtFragment = new DistrictFragment();
            fm.beginTransaction().add(R.id.fragContainerMainMenu, districtFragment).commit();
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
            case R.id.profile:
                replaceToProfileFragment();
                break;
            case R.id.make_appointment:
                if(loggedIn) {
                    replaceToSpecialityFragment();
                } else Toast.makeText(this, R.string.toast_you_have_to_login_for_action, Toast.LENGTH_SHORT).show();
                break;
            case R.id.doctor_schedule:
                Intent scheduleIntent = new Intent(this, ScheduleActivity.class);
                startActivity(scheduleIntent);
                break;
            case R.id.appointment_history:
                if (loggedIn) {
                    replaceToAppointmentHistoryFragment();
                } else Toast.makeText(this, R.string.toast_you_have_to_login_for_action, Toast.LENGTH_SHORT).show();
                break;
            case R.id.reviews:
                Intent intent = new Intent(this, ReviewActivity.class);
                intent.putExtra("fragToLoad", ReviewActivity.FRAGMENT_MAIN_MENU);
                startActivity(intent);
                break;
            case R.id.checkup:
                replaceToCheckupFragment();
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

    private void replaceToProfileFragment() {
        ProfileFragment profileFragment = ProfileFragment.newInstance(patient);
        fm.beginTransaction().replace(R.id.fragContainerMainMenu, profileFragment).commit();
    }

    private void replaceToCheckupFragment() {
        CheckupFragment checkupFragment = null;
        if (loggedIn) {
            checkupFragment = CheckupFragment.newInstance(AgeCalculator.getAge(new LocalDate(patient.getYearBirth(), patient.getMonthBirth(), patient.getDayBirth()), new LocalDate()));
        } else if (guestMode) {
            checkupFragment = CheckupFragment.newInstance(20);
        }
        fm.beginTransaction().replace(R.id.fragContainerMainMenu, checkupFragment).commit();
    }

    private void replaceToSpecialityFragment() {
        String hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        SpecialityFragment specialityFragment = SpecialityFragment.newInstance(hospitalId, patient.getServiceId());
        fm.beginTransaction().replace(R.id.fragContainerMainMenu, specialityFragment).commit();
    }

    public void replaceToAppointmentHistoryFragment() {
        AppointmentHistoryFragment appHistoryFrag = AppointmentHistoryFragment.newInstance(patient.getServiceId());
        fm.beginTransaction().replace(R.id.fragContainerMainMenu, appHistoryFrag).addToBackStack(null).commit();
    }

    private void replaceToChooseAppointmentFragment(String doctorId, String hospitalId) {
        ChooseAppointmentFragment chooseAppointmentFragment = ChooseAppointmentFragment.newInstance(doctorId, hospitalId, patient.getServiceId());
        fm.beginTransaction().replace(R.id.fragContainerMainMenu, chooseAppointmentFragment).addToBackStack("doctor_fragment").commit();
    }

    @Override
    public void onDistrictFragmentDataListener(District district) {
        patient.setDistrict(district);
        HospitalFragment hospitalFragment = HospitalFragment.newInstance(district);
        fTrans = fm.beginTransaction().replace(R.id.fragContainerMainMenu, hospitalFragment).addToBackStack("fragment_district");
        fTrans.commit();

        editor = mSettings.edit();
        editor.putString(APP_SETTINGS_DISTRICT_ID, district.getId());
        editor.apply();
    }

    @Override
    public void onHospitalFragmentDataListener(Hospital hospital) {
        patient.setHospital(hospital);
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        replaceToProfileFragment();

        if(!loggedIn) {
            editor = mSettings.edit();
            editor.putBoolean(APP_SETTINGS_GUEST_MODE, true);
            editor.putString(APP_SETTINGS_DISTRICT_NAME, patient.getDistrict().getName());
            editor.putInt(APP_SETTINGS_HOSPITAL_ID, hospital.getIdLPU());
            editor.putString(APP_SETTINGS_HOSPITAL_NAME_SHORT, hospital.getLPUShortName());
            editor.apply();
        }

        TextView tvPatientFIO = navigationView.getHeaderView(0).findViewById(R.id.tvPatientFIO);
        tvPatientFIO.setText("Гость");
        TextView tvPatientAddress = navigationView.getHeaderView(0).findViewById(R.id.tvPatientAddress);
        tvPatientAddress.setText(hospital.getLPUShortName() + ", " + patient.getDistrict().getName());
    }

    @Override
    public void onSpecialityFragmentDataListener(Speciality speciality) {
        this.speciality = speciality;
        String hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        String specialityId = speciality.getIdSpeciality();
        DoctorFragment doctorFragment = DoctorFragment.newInstance(hospitalId, patient.getServiceId(), specialityId);
        fm.beginTransaction().replace(R.id.fragContainerMainMenu, doctorFragment).addToBackStack("spec_fragment").commit();
    }

    @Override
    public void onDoctorFragmentDataListener(Doctor doctor) {
        this.doctor = doctor;
        String doctorId = doctor.getIdDoc();
        String hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        replaceToChooseAppointmentFragment(doctorId, hospitalId);
    }

    @Override
    public void onAppointmentFragmentDataListener(AppointmentInfo appInfo) {
        Appointment app = new Appointment();
        app.setAppString(appInfo.getId());
        app.setServiceId(patient.getServiceId());
        app.setDistrictId(Integer.valueOf(patient.getDistrict().getId()));
        app.setDistrictName(patient.getDistrict().getName());
        app.setLpuId(patient.getHospital().getIdLPU());
        app.setLpuNameShort(patient.getHospital().getLPUShortName());
        app.setLpuNameFull(patient.getHospital().getLpuName());
        app.setSpecId(Integer.valueOf(speciality.getIdSpeciality()));
        app.setSpecName(speciality.getNameSpeciality());
        app.setDocId(Integer.valueOf(doctor.getIdDoc()));
        app.setDocName(doctor.getName());
        app.setDateTime(appInfo.getDateStart().getDateTime());
        createAppointment(app);
        /*for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }*/
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        replaceToAppointmentHistoryFragment();
    }

    private void createAppointment(Appointment app) {
        springApi.createAppointment(app).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response resp = response.body();
                if (response.isSuccessful()) {
                    if (resp.isSuccess()) {
                        Toast.makeText(MainActivity.this, "Запись сделана", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "Запись сделана");
                    } else {
                        Toast.makeText(MainActivity.this, "Запись не сделана", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "Запись не сделана");
                    }
                }
                else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this, "Ошибка сервера", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onProfileFragmentBtnClick(View v) {
        switch (v.getId()) {
            case R.id.btnMyAppointments:
                if(!guestMode)
                    replaceToAppointmentHistoryFragment();
                else Toast.makeText(this, R.string.toast_you_have_to_login_for_action, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnMyReviews:
                if (!guestMode) {
                    Intent myReviewsIntent = new Intent(this, ReviewActivity.class);
                    myReviewsIntent.putExtra("fragToLoad", ReviewActivity.FRAGMENT_MY_REVIEWS);
                    startActivity(myReviewsIntent);
                } else Toast.makeText(this, R.string.toast_you_have_to_login_for_action, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnMyComments:
                if (!guestMode) {
                    Intent myCommentsIntent = new Intent(this, ReviewActivity.class);
                    myCommentsIntent.putExtra("fragToLoad", ReviewActivity.FRAGMENT_MY_COMMENTS);
                    startActivity(myCommentsIntent);
                } else Toast.makeText(this, R.string.toast_you_have_to_login_for_action, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
