package com.bigblackboy.doctorappointment.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.SharedPreferencesManager;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;
import com.bigblackboy.doctorappointment.presenter.ScheduleActivityPresenter;
import com.bigblackboy.doctorappointment.view.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.view.fragment.DoctorFragment;
import com.bigblackboy.doctorappointment.view.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.view.fragment.ReviewMainFragment;
import com.bigblackboy.doctorappointment.view.fragment.ScheduleFragment;
import com.bigblackboy.doctorappointment.view.fragment.SpecialityFragment;

public class ScheduleActivity extends AppCompatActivity implements ReviewMainFragment.OnReviewMainFragmentDataListener, DistrictFragment.OnDistrictFragmentDataListener,
        SpecialityFragment.OnSpecialityFragmentDataListener, HospitalFragment.OnHospitalFragmentDataListener, DoctorFragment.OnDoctorFragmentDataListener {

    private FragmentManager fm;
    private LinearLayout fragContainer;
    private ScheduleActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        SharedPreferences mSettings = getSharedPreferences(SharedPreferencesManager.APP_SETTINGS, Context.MODE_PRIVATE);
        presenter = new ScheduleActivityPresenter(mSettings);
        presenter.attachView(this);

        fm = getSupportFragmentManager();
        fragContainer = findViewById(R.id.fragContainerSchedule);
        addFragmentToContainer(new ReviewMainFragment(), fragContainer.getId());
    }

    @Override
    public void onReviewMainFragmentBtnClick(View v) {
        switch (v.getId()) {
            case R.id.btnMyDoctorReviewMain:
                presenter.onBtnMyDoctorReviewMainClick();
                break;
            case R.id.btnChangeHospitalReviewMain:
                presenter.onBtnChangeHospitalReviewMainClick();
                break;
        }
    }

    private void addFragmentToContainer(Fragment fragment, int containerId) {
        fm.beginTransaction().add(containerId, fragment).commit();
    }

    private void replaceFragment(Fragment fragment, int containerId, String backstackTitle) {
        fm.beginTransaction().replace(containerId, fragment).addToBackStack(backstackTitle).commit();
    }

    public void replaceToDistrictFragment() {
        DistrictFragment districtFragment = new DistrictFragment();
        fm.beginTransaction().replace(fragContainer.getId(), districtFragment).addToBackStack("review_main_menu").commit();
    }

    public void replaceToHospitalFragment(District district) {
        HospitalFragment hospitalFragment = HospitalFragment.newInstance(district);
        replaceFragment(hospitalFragment, fragContainer.getId(), "district_fragment");
    }

    public void replaceToSpecialityFragment(String hospitalId, String patientServiceId) {
        SpecialityFragment specialityFragment = SpecialityFragment.newInstance(hospitalId, patientServiceId);
        replaceFragment(specialityFragment, fragContainer.getId(), "ReviewActivityMain");
    }

    public void replaceToDoctorFragment(String hospitalId, String patientServiceId, String specialityId) {
        DoctorFragment doctorFragment = DoctorFragment.newInstance(hospitalId, patientServiceId, specialityId);
        replaceFragment(doctorFragment, fragContainer.getId(), "spec_fragment");
    }

    public void replaceToScheduleFragment(Doctor doctor, String hospitalId) {
        ScheduleFragment fragment = ScheduleFragment.newInstance(doctor, hospitalId);
        replaceFragment(fragment, fragContainer.getId(), "doctor_fragment");
    }

    @Override
    public void onDistrictFragmentDataListener(District district) {
        presenter.onGetDistrictFragmentData(district);
    }

    @Override
    public void onSpecialityFragmentDataListener(Speciality speciality) {
        presenter.onGetSpecialityFragmentData(speciality);
    }


    @Override
    public void onHospitalFragmentDataListener(Hospital hospital) {
        presenter.onGetHospitalFragmentData(hospital);
    }

    @Override
    public void onDoctorFragmentDataListener(Doctor doctor) {
        presenter.onGetDoctorFragmentData(doctor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
