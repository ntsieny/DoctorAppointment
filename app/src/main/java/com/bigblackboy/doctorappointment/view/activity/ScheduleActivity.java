package com.bigblackboy.doctorappointment.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.utils.SharedPreferencesManager;
import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.bigblackboy.doctorappointment.controller.HospitalController;
import com.bigblackboy.doctorappointment.view.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.view.fragment.DoctorFragment;
import com.bigblackboy.doctorappointment.view.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.view.fragment.ReviewMainFragment;
import com.bigblackboy.doctorappointment.view.fragment.ScheduleFragment;
import com.bigblackboy.doctorappointment.view.fragment.SpecialityFragment;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;

public class ScheduleActivity extends AppCompatActivity implements ReviewMainFragment.OnReviewMainFragmentDataListener, DistrictFragment.OnDistrictFragmentDataListener,
        SpecialityFragment.OnSpecialityFragmentDataListener, HospitalFragment.OnHospitalFragmentDataListener, DoctorFragment.OnDoctorFragmentDataListener {

    private FragmentManager fm;
    private District district;
    private Hospital hospital;
    private Speciality speciality;
    private Doctor doctor;
    private LinearLayout fragContainer;
    private boolean useSharedPrefsPatientInfo = true;
    private Patient patient;
    private SharedPreferences prefs;
    private SharedPreferencesManager prefManager;
    private HospitalApi hospitalApi;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int backstackCount = fm.getBackStackEntryCount();
        if (backstackCount == 0) {
            super.onBackPressed();
        } else fm.popBackStack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        prefs = getSharedPreferences(SharedPreferencesManager.APP_SETTINGS, Context.MODE_PRIVATE);
        prefManager = new SharedPreferencesManager(prefs);
        patient = prefManager.getCurrentPatient();
        hospital = prefManager.getCurrentHospital();

        hospitalApi = HospitalController.getApi();

        fm = getSupportFragmentManager();
        fragContainer = findViewById(R.id.fragContainerSchedule);
        addFragmentToContainer(new ReviewMainFragment(), fragContainer.getId());
    }

    @Override
    public void onReviewMainFragmentBtnClick(View v) {
        switch (v.getId()) {
            case R.id.btnMyDoctorReviewMain:
                useSharedPrefsPatientInfo = true;
                replaceToSpecialityFragment();
                break;
            case R.id.btnChangeHospitalReviewMain:
                useSharedPrefsPatientInfo = false;
                replaceToDistrictFragment();
                break;
        }
    }

    private void addFragmentToContainer(Fragment fragment, int containerId) {
        fm.beginTransaction().add(containerId, fragment).commit();
    }

    private void replaceFragment(Fragment fragment, int containerId, String backstackTitle) {
        fm.beginTransaction().replace(containerId, fragment).addToBackStack(backstackTitle).commit();
    }

    public void replaceToSpecialityFragment() {
        String hospitalId;
        if (useSharedPrefsPatientInfo) {
            hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        } else hospitalId = String.valueOf(hospital.getIdLPU());
        SpecialityFragment specialityFragment = SpecialityFragment.newInstance(hospitalId, patient.getServiceId());
        fm.beginTransaction().replace(fragContainer.getId(), specialityFragment).addToBackStack("ReviewActivityMain").commit();
    }

    public void replaceToDistrictFragment() {
        DistrictFragment districtFragment = new DistrictFragment();
        fm.beginTransaction().replace(fragContainer.getId(), districtFragment).addToBackStack("review_main_menu").commit();
    }

    @Override
    public void onDistrictFragmentDataListener(District district) {
        this.district = district;
        HospitalFragment hospitalFragment = HospitalFragment.newInstance(district);
        replaceFragment(hospitalFragment, fragContainer.getId(), null);
    }

    @Override
    public void onSpecialityFragmentDataListener(Speciality speciality) {
        this.speciality = speciality;
        String hospitalId;
        if (useSharedPrefsPatientInfo) {
            hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        } else hospitalId = String.valueOf(hospital.getIdLPU());
        String specialityId = speciality.getIdSpeciality();
        DoctorFragment doctorFragment = DoctorFragment.newInstance(hospitalId, patient.getServiceId(), specialityId);
        replaceFragment(doctorFragment, fragContainer.getId(), "spec_fragment");
    }


    @Override
    public void onHospitalFragmentDataListener(Hospital hospital) {
        this.hospital = hospital;
        replaceToSpecialityFragment();
    }

    @Override
    public void onDoctorFragmentDataListener(Doctor doctor) {
        this.doctor = doctor;
        ScheduleFragment fragment = ScheduleFragment.newInstance(doctor, String.valueOf(hospital.getIdLPU()));
        replaceFragment(fragment, fragContainer.getId(), null);
    }
}
