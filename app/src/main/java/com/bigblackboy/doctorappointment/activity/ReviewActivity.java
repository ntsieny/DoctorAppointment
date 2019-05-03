package com.bigblackboy.doctorappointment.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.SharedPreferencesManager;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.fragment.DoctorFragment;
import com.bigblackboy.doctorappointment.fragment.DoctorReviewsFragment;
import com.bigblackboy.doctorappointment.fragment.ReviewMainFragment;
import com.bigblackboy.doctorappointment.fragment.SpecialityFragment;
import com.bigblackboy.doctorappointment.model.Doctor;
import com.bigblackboy.doctorappointment.model.Patient;
import com.bigblackboy.doctorappointment.model.Speciality;

public class ReviewActivity extends AppCompatActivity implements SpecialityFragment.OnSpecialityFragmentDataListener, DoctorFragment.OnDoctorFragmentDataListener {

    public static final int FRAGMENT_DOCTOR_REVIEWS = 0;
    public static final int FRAGMENT_MY_REVIEWS = 1;
    public static final int FRAGMENT_MY_COMMENTS = 2;
    public static final int FRAGMENT_MAIN_MENU = 3;

    private static final String LOG_TAG = "myLog: ReviewActivity";
    private FragmentManager fm;
    private SpringApi springApi;
    SharedPreferences mSettings;
    private boolean loggedIn;
    private boolean guestMode;
    private Patient patient;
    private Speciality speciality;
    private Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        fm = getSupportFragmentManager();
        springApi = SpringController.getApi();

        mSettings = getSharedPreferences(SharedPreferencesManager.APP_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferencesManager prefManager = new SharedPreferencesManager(mSettings);
        patient = prefManager.getCurrentPatient();

        loggedIn = prefManager.isUserLoggedIn();
        guestMode = prefManager.isGuestMode();

        if (getIntent().hasExtra("fragToLoad")) {
            int fragToLoad = getIntent().getExtras().getInt("fragToLoad");
            switch (fragToLoad) {
                case FRAGMENT_MAIN_MENU:
                    addFragmentToContainer(new ReviewMainFragment(), R.id.fragContainerReview);
                    break;
                case FRAGMENT_MY_REVIEWS:

                    break;
                case FRAGMENT_MY_COMMENTS:

                    break;
                case FRAGMENT_DOCTOR_REVIEWS:

                    break;
            }
        }
    }

    private void addFragmentToContainer(Fragment fragment, int containerId) {
        fm.beginTransaction().add(containerId, fragment).commit();
    }

    public void replaceToSpecialityFragment() {
        String hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        SpecialityFragment specialityFragment = SpecialityFragment.newInstance(hospitalId, patient.getServiceId());
        fm.beginTransaction().replace(R.id.fragContainerReview, specialityFragment).addToBackStack("ReviewActivityMain").commit();
    }

    private void replaceToDoctorReviewsFragment() {
        DoctorReviewsFragment fragment = DoctorReviewsFragment.newInstance(doctor.getIdDoc(), doctor.getName());
        fm.beginTransaction().replace(R.id.fragContainerReview, fragment).addToBackStack("doctor_fragment").commit();
    }

    @Override
    public void onSpecialityFragmentDataListener(Speciality speciality) {
        this.speciality = speciality;
        String hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        String specialityId = speciality.getIdSpeciality();
        DoctorFragment doctorFragment = DoctorFragment.newInstance(hospitalId, patient.getServiceId(), specialityId);
        fm.beginTransaction().replace(R.id.fragContainerReview, doctorFragment).addToBackStack("spec_fragment").commit();
    }

    @Override
    public void onDoctorFragmentDataListener(Doctor doctor) {
        this.doctor = doctor;
        String doctorId = doctor.getIdDoc();
        String hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        replaceToDoctorReviewsFragment();
    }


}
