package com.bigblackboy.doctorappointment.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.SharedPreferencesManager;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.fragment.DoctorFragment;
import com.bigblackboy.doctorappointment.fragment.DoctorReviewDetailedFragment;
import com.bigblackboy.doctorappointment.fragment.DoctorReviewsFragment;
import com.bigblackboy.doctorappointment.fragment.EditReviewFragment;
import com.bigblackboy.doctorappointment.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.fragment.ReviewMainFragment;
import com.bigblackboy.doctorappointment.fragment.SpecialityFragment;
import com.bigblackboy.doctorappointment.fragment.UserCommentsFragment;
import com.bigblackboy.doctorappointment.fragment.UserReviewsFragment;
import com.bigblackboy.doctorappointment.model.District;
import com.bigblackboy.doctorappointment.model.Doctor;
import com.bigblackboy.doctorappointment.model.Hospital;
import com.bigblackboy.doctorappointment.model.Patient;
import com.bigblackboy.doctorappointment.model.Speciality;
import com.bigblackboy.doctorappointment.springserver.springmodel.MyCommentsResponse;
import com.bigblackboy.doctorappointment.springserver.springmodel.Review;
import com.bigblackboy.doctorappointment.springserver.springmodel.ReviewsResponse;

public class ReviewActivity extends AppCompatActivity implements DistrictFragment.OnDistrictFragmentDataListener, HospitalFragment.OnHospitalFragmentDataListener, SpecialityFragment.OnSpecialityFragmentDataListener, DoctorFragment.OnDoctorFragmentDataListener,
        DoctorReviewsFragment.OnDoctorReviewsFragmentDataListener, DoctorReviewDetailedFragment.OnDoctorReviewDetailedFragmentDataListener, ReviewMainFragment.OnReviewMainFragmentDataListener,
        UserReviewsFragment.OnUserReviewsFragmentDataListener, UserCommentsFragment.OnUserCommentsFragmentDataListener {

    public static final int FRAGMENT_DOCTOR_REVIEWS = 0;
    public static final int FRAGMENT_MY_REVIEWS = 1;
    public static final int FRAGMENT_MY_COMMENTS = 2;
    public static final int FRAGMENT_MAIN_MENU = 3;

    private static final String LOG_TAG = "myLog: ReviewActivity";
    private FragmentManager fm;
    private SpringApi springApi;
    SharedPreferences mSettings;
    SharedPreferencesManager prefManager;
    private boolean loggedIn;
    private boolean guestMode;
    private Patient patient;
    private Speciality speciality;
    private Doctor doctor;
    private District district;
    private Hospital hospital;
    private boolean useSharedPrefsPatientInfo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        fm = getSupportFragmentManager();
        springApi = SpringController.getApi();

        mSettings = getSharedPreferences(SharedPreferencesManager.APP_SETTINGS, Context.MODE_PRIVATE);
        prefManager = new SharedPreferencesManager(mSettings);
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
                    replaceToUserReviewsFragment(patient.getServiceId());
                    break;
                case FRAGMENT_MY_COMMENTS:
                    replaceToUserCommentsFragment(patient.getServiceId());
                    break;
                case FRAGMENT_DOCTOR_REVIEWS:
                    if(getIntent().hasExtra("doctorId") && getIntent().hasExtra("doctorName")) {
                        this.doctor = new Doctor();
                        String doctorId = getIntent().getStringExtra("doctorId");
                        String doctorName = getIntent().getStringExtra("doctorName");
                        doctor.setIdDoc(doctorId);
                        doctor.setName(doctorName);
                        String specId = getIntent().getStringExtra("specialityId");
                        String specName = getIntent().getStringExtra("specialityName");
                        this.speciality = new Speciality();
                        speciality.setIdSpeciality(specId);
                        speciality.setNameSpeciality(specName);
                        addFragmentToContainer(DoctorReviewsFragment.newInstance(doctorId, doctorName, prefManager.getCurrentPatient().getServiceId()), R.id.fragContainerReview);
                    }
                    break;
            }
        }
    }

    private void replaceToUserReviewsFragment(String serviceId) {
        UserReviewsFragment userReviewsFragment = UserReviewsFragment.newInstance(serviceId);
        fm.beginTransaction().replace(R.id.fragContainerReview, userReviewsFragment).commit();
    }

    private void replaceToUserCommentsFragment(String serviceId) {
        UserCommentsFragment userCommentsFragment = UserCommentsFragment.newInstance(serviceId);
        fm.beginTransaction().replace(R.id.fragContainerReview, userCommentsFragment).commit();
    }

    private void addFragmentToContainer(Fragment fragment, int containerId) {
        fm.beginTransaction().add(containerId, fragment).commit();
    }

    public void replaceToDistrictFragment() {
        DistrictFragment districtFragment = new DistrictFragment();
        fm.beginTransaction().replace(R.id.fragContainerReview, districtFragment).addToBackStack("review_main_menu").commit();
    }

    private void replaceToHospitalFragment(District district) {
        HospitalFragment hospitalFragment = HospitalFragment.newInstance(district);
        fm.beginTransaction().replace(R.id.fragContainerReview, hospitalFragment).addToBackStack("district_fragment").commit();
    }

    public void replaceToSpecialityFragment() {
        String hospitalId;
        if (useSharedPrefsPatientInfo) {
            hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        } else hospitalId = String.valueOf(hospital.getIdLPU());
        SpecialityFragment specialityFragment = SpecialityFragment.newInstance(hospitalId, patient.getServiceId());
        fm.beginTransaction().replace(R.id.fragContainerReview, specialityFragment).addToBackStack("ReviewActivityMain").commit();
    }

    private void replaceToEditReviewFragment() {
        EditReviewFragment editReviewFragment;
        if(useSharedPrefsPatientInfo) {
            editReviewFragment = EditReviewFragment.newInstance(prefManager.getCurrentDistrict(), speciality, doctor, prefManager.getCurrentHospital(), prefManager.getCurrentPatient().getServiceId());
        } else editReviewFragment = EditReviewFragment.newInstance(district, speciality, doctor, hospital, prefManager.getCurrentPatient().getServiceId());
        fm.beginTransaction().replace(R.id.fragContainerReview, editReviewFragment).addToBackStack("doctor_reviews_fragment").commit();
    }

    private void replaceToEditReviewFragment(Review review) {
        EditReviewFragment editReviewFragment = EditReviewFragment.newInstance(prefManager.getCurrentPatient().getServiceId(), review);
        fm.beginTransaction().replace(R.id.fragContainerReview, editReviewFragment).addToBackStack("doctor_reviews_fragment").commit();
    }

    private void replaceToDoctorReviewsFragment() {
        DoctorReviewsFragment fragment = DoctorReviewsFragment.newInstance(doctor.getIdDoc(), doctor.getName(), prefManager.getCurrentPatient().getServiceId());
        fm.beginTransaction().replace(R.id.fragContainerReview, fragment).addToBackStack("doctor_fragment").commit();
    }

    private void replaceToDoctorReviewsFragment(String doctorId, String doctorName) {
        DoctorReviewsFragment fragment = DoctorReviewsFragment.newInstance(doctorId, doctorName, prefManager.getCurrentPatient().getServiceId());
        fm.beginTransaction().replace(R.id.fragContainerReview, fragment).addToBackStack("my_reviews_fragment").commit();
    }

    private void replaceToDoctorReviewDetailedFragment(int reviewId) {
        DoctorReviewDetailedFragment fragment = DoctorReviewDetailedFragment.newInstance(reviewId, prefManager.getCurrentPatient().getServiceId());
        fm.beginTransaction().replace(R.id.fragContainerReview, fragment).addToBackStack("review_full_fragment").commit();
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
        fm.beginTransaction().replace(R.id.fragContainerReview, doctorFragment).addToBackStack("spec_fragment").commit();
    }

    @Override
    public void onDoctorFragmentDataListener(Doctor doctor) {
        this.doctor = doctor;
        String doctorId = doctor.getIdDoc();
        String hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        replaceToDoctorReviewsFragment();
    }


    @Override
    public void onDoctorReviewsFragmentBtnClickListener(View v, ReviewsResponse review) {
        switch (v.getId()) {
            case R.id.btnAddReview:
                if (!guestMode) {
                    replaceToEditReviewFragment();
                } else Toast.makeText(this, R.string.toast_you_have_to_login_for_action, Toast.LENGTH_SHORT).show();
                break;
            case R.id.imBtnComments:
                replaceToDoctorReviewDetailedFragment(review.getReviewId());
                break;
        }
    }

    @Override
    public void onDoctorReviewsFragmentDataListener(ReviewsResponse review) {
        replaceToDoctorReviewDetailedFragment(review.getReviewId());
    }

    @Override
    public void onDoctorReviewDetailedFragmentBtnClick(View v, ReviewsResponse rev) {
        switch (v.getId()) {
            case R.id.imBtnEditReview:
                replaceToEditReviewFragment(rev);
                break;
        }
    }

    @Override
    public void onDistrictFragmentDataListener(District district) {
        this.district = district;
        replaceToHospitalFragment(district);
    }

    @Override
    public void onHospitalFragmentDataListener(Hospital hospital) {
        this.hospital = hospital;
        replaceToSpecialityFragment();
    }

    @Override
    public void onReviewMainFragmentBtnClick(View v) {
        switch (v.getId()) {
            case R.id.btnMyDoctorReviewMain:
                replaceToSpecialityFragment();
                break;
            case R.id.btnChangeHospitalReviewMain:
                useSharedPrefsPatientInfo = false;
                replaceToDistrictFragment();
                break;
        }
    }

    @Override
    public void onUserReviewsFragmentBtnClick(View v, Review review) {
        switch (v.getId()) {
            case R.id.imBtnEditReview:
                replaceToEditReviewFragment(review);
                break;
        }
    }

    @Override
    public void onUserReviewsFragmentDataListener(Review review) {
        replaceToDoctorReviewsFragment(String.valueOf(review.getDoctorId()), review.getDoctorName());
    }

    @Override
    public void onUserCommentsFragmentDataListener(int reviewId) {
        replaceToDoctorReviewDetailedFragment(reviewId);
    }
}
