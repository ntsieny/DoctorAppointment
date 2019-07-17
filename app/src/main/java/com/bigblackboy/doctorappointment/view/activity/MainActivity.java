package com.bigblackboy.doctorappointment.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.SharedPreferencesManager;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.AppointmentInfo;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;
import com.bigblackboy.doctorappointment.presenter.MainActivityPresenter;
import com.bigblackboy.doctorappointment.view.fragment.AppointmentHistoryFragment;
import com.bigblackboy.doctorappointment.view.fragment.CheckupFragment;
import com.bigblackboy.doctorappointment.view.fragment.ChooseAppointmentFragment;
import com.bigblackboy.doctorappointment.view.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.view.fragment.DoctorFragment;
import com.bigblackboy.doctorappointment.view.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.view.fragment.ProfileFragment;
import com.bigblackboy.doctorappointment.view.fragment.SpecialityFragment;

public class MainActivity extends AppCompatActivity implements MVPBaseInterface.View, NavigationView.OnNavigationItemSelectedListener, DistrictFragment.OnDistrictFragmentDataListener,
        HospitalFragment.OnHospitalFragmentDataListener, SpecialityFragment.OnSpecialityFragmentDataListener, DoctorFragment.OnDoctorFragmentDataListener,
        ChooseAppointmentFragment.OnAppointmentFragmentDataListener, ProfileFragment.OnProfileFragmentDataListener {

    private static final String LOG_TAG = "myLog: MainActivity";
    private MainActivityPresenter presenter;
    private FragmentManager fm;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        fm = getSupportFragmentManager();

        SharedPreferences mSettings = getSharedPreferences(SharedPreferencesManager.APP_SETTINGS, Context.MODE_PRIVATE);
        presenter = new MainActivityPresenter(mSettings);
        presenter.attachView(this);
        presenter.onCreateActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutItem:
                presenter.onLogoutItemClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.profile:
                presenter.onProfileItemClick();
                break;
            case R.id.make_appointment:
                presenter.onMakeAppointmentItemClick();
                break;
            case R.id.doctor_schedule:
                presenter.onDoctorScheduleItemClick();
                break;
            case R.id.appointment_history:
                presenter.onAppointmentHistoryItemClick();
                break;
            case R.id.reviews:
                presenter.onReviewsItemClick();
                break;
            case R.id.checkup:
                presenter.onCheckupItemClick();
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

    public void addDistrictFragment() {
        DistrictFragment districtFragment = new DistrictFragment();
        fm.beginTransaction().add(R.id.fragContainerMainMenu, districtFragment).commit();
    }

    public void startScheduleActivity() {
        Intent scheduleIntent = new Intent(this, ScheduleActivity.class);
        startActivity(scheduleIntent);
    }

    public void startReviewActivity(int fragmentToLoad) {
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra("fragToLoad", fragmentToLoad);
        startActivity(intent);
    }

    public void startChooseLoginActivity() {
        finish();
        Intent intent = new Intent(this, ChooseLoginActivity.class);
        startActivity(intent);
    }

    public void replaceToProfileFragment(Patient patient) {
        ProfileFragment profileFragment = ProfileFragment.newInstance(patient);
        fm.beginTransaction().replace(R.id.fragContainerMainMenu, profileFragment).commit();
    }

    public void replaceToCheckupFragment(int patientAge) {
        CheckupFragment checkupFragment = CheckupFragment.newInstance(patientAge);
        fm.beginTransaction().replace(R.id.fragContainerMainMenu, checkupFragment).commit();
    }

    public void replaceToSpecialityFragment(Patient patient) {
        String hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        SpecialityFragment specialityFragment = SpecialityFragment.newInstance(hospitalId, patient.getServiceId());
        fm.beginTransaction().replace(R.id.fragContainerMainMenu, specialityFragment).commit();
    }

    public void replaceToAppointmentHistoryFragment(String patientServiceId) {
        AppointmentHistoryFragment appHistoryFrag = AppointmentHistoryFragment.newInstance(patientServiceId);
        fm.beginTransaction().replace(R.id.fragContainerMainMenu, appHistoryFrag).addToBackStack(null).commit();
    }

    public void replaceToChooseAppointmentFragment(String doctorId, String hospitalId, String patientServiceId) {
        ChooseAppointmentFragment chooseAppointmentFragment = ChooseAppointmentFragment.newInstance(doctorId, hospitalId, patientServiceId);
        fm.beginTransaction().replace(R.id.fragContainerMainMenu, chooseAppointmentFragment).addToBackStack("doctor_fragment").commit();
    }

    public void replaceToHospitalFragment(District district) {
        HospitalFragment hospitalFragment = HospitalFragment.newInstance(district);
        fm.beginTransaction().replace(R.id.fragContainerMainMenu, hospitalFragment).addToBackStack("fragment_district").commit();
    }

    public void replaceToDoctorFragment(String hospitalId, String patientServiceId, String chosenSpecialityId) {
        DoctorFragment doctorFragment = DoctorFragment.newInstance(hospitalId, patientServiceId, chosenSpecialityId);
        fm.beginTransaction().replace(R.id.fragContainerMainMenu, doctorFragment).addToBackStack("spec_fragment").commit();
    }

    @Override
    public void onDistrictFragmentDataListener(District district) {
        presenter.onGetDistrictFragmentData(district);
    }

    @Override
    public void onHospitalFragmentDataListener(Hospital hospital) {
        presenter.onGetHospitalFragmentData(hospital);
    }

    @Override
    public void onSpecialityFragmentDataListener(Speciality speciality) {
        presenter.onGetSpecialityFragmentData(speciality);
    }

    @Override
    public void onDoctorFragmentDataListener(Doctor doctor) {
        presenter.onGetDoctorFragmentData(doctor);
    }

    @Override
    public void onAppointmentFragmentDataListener(AppointmentInfo appInfo) {
        presenter.onGetAppointmentFragmentData(appInfo);
    }

    @Override
    public void onProfileFragmentBtnClick(View v) {
        switch (v.getId()) {
            case R.id.btnMyAppointments:
                presenter.onMyAppointmentsBtnClick();
                break;
            case R.id.btnMyReviews:
                presenter.onMyReviewsBtnClick();
                break;
            case R.id.btnMyComments:
                presenter.onMyCommentsBtnClick();
                break;
        }
    }

    public void setNavigationDrawer() {
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

    public void popBackStack() {
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void setPatientFIO(String fio) {
        TextView tvPatientFIO = navigationView.getHeaderView(0).findViewById(R.id.tvPatientFIO);
        tvPatientFIO.setText(fio);
    }

    public void setPatientAddress(String address) {
        TextView tvPatientAddress = navigationView.getHeaderView(0).findViewById(R.id.tvPatientAddress);
        tvPatientAddress.setText(address);
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
