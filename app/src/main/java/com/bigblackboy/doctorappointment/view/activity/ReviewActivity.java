package com.bigblackboy.doctorappointment.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.SharedPreferencesManager;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;
import com.bigblackboy.doctorappointment.pojos.springpojos.Review;
import com.bigblackboy.doctorappointment.pojos.springpojos.ReviewsResponse;
import com.bigblackboy.doctorappointment.presenter.ReviewActivityPresenter;
import com.bigblackboy.doctorappointment.view.fragment.DistrictFragment;
import com.bigblackboy.doctorappointment.view.fragment.DoctorFragment;
import com.bigblackboy.doctorappointment.view.fragment.DoctorReviewDetailedFragment;
import com.bigblackboy.doctorappointment.view.fragment.DoctorReviewsFragment;
import com.bigblackboy.doctorappointment.view.fragment.EditReviewFragment;
import com.bigblackboy.doctorappointment.view.fragment.HospitalFragment;
import com.bigblackboy.doctorappointment.view.fragment.ReviewMainFragment;
import com.bigblackboy.doctorappointment.view.fragment.SpecialityFragment;
import com.bigblackboy.doctorappointment.view.fragment.UserCommentsFragment;
import com.bigblackboy.doctorappointment.view.fragment.UserReviewsFragment;

public class ReviewActivity extends AppCompatActivity implements MVPBaseInterface.View, DistrictFragment.OnDistrictFragmentDataListener, HospitalFragment.OnHospitalFragmentDataListener, SpecialityFragment.OnSpecialityFragmentDataListener, DoctorFragment.OnDoctorFragmentDataListener,
        DoctorReviewsFragment.OnDoctorReviewsFragmentDataListener, DoctorReviewDetailedFragment.OnDoctorReviewDetailedFragmentDataListener, ReviewMainFragment.OnReviewMainFragmentDataListener,
        UserReviewsFragment.OnUserReviewsFragmentDataListener, UserCommentsFragment.OnUserCommentsFragmentDataListener {

    public static final int FRAGMENT_DOCTOR_REVIEWS = 0;
    public static final int FRAGMENT_MY_REVIEWS = 1;
    public static final int FRAGMENT_MY_COMMENTS = 2;
    public static final int FRAGMENT_MAIN_MENU = 3;

    private static final String LOG_TAG = "myLog: ReviewActivity";
    private FragmentManager fm;
    private ReviewActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        fm = getSupportFragmentManager();

        SharedPreferences mSettings = getSharedPreferences(SharedPreferencesManager.APP_SETTINGS, Context.MODE_PRIVATE);
        presenter = new ReviewActivityPresenter(mSettings);
        presenter.attachView(this);

        if (getIntent().hasExtra("fragToLoad")) {
            int fragToLoad = getIntent().getExtras().getInt("fragToLoad");
            switch (fragToLoad) {
                case FRAGMENT_MAIN_MENU:
                    presenter.onMainMenuFragmentLoad();
                    break;
                case FRAGMENT_MY_REVIEWS:
                    presenter.onMyReviewsFragmentLoad();
                    break;
                case FRAGMENT_MY_COMMENTS:
                    presenter.onMyCommentsFragmentLoad();
                    break;
                case FRAGMENT_DOCTOR_REVIEWS:
                    if(getIntent().hasExtra("doctorId") && getIntent().hasExtra("doctorName")) {
                        Doctor doctor = new Doctor();
                        String doctorId = getIntent().getStringExtra("doctorId");
                        String doctorName = getIntent().getStringExtra("doctorName");
                        doctor.setIdDoc(doctorId);
                        doctor.setName(doctorName);

                        Speciality speciality = new Speciality();
                        String specId = getIntent().getStringExtra("specialityId");
                        String specName = getIntent().getStringExtra("specialityName");
                        speciality.setIdSpeciality(specId);
                        speciality.setNameSpeciality(specName);
                        presenter.onDoctorReviewsFragmentLoad(doctor, speciality);
                    }
                    break;
            }
        }
    }

    public void addReviewMainFragment() {
        addFragmentToContainer(new ReviewMainFragment(), R.id.fragContainerReview);
    }

    public void addDoctorReviewsFragment(String doctorId, String doctorName, String patientServiceId) {
        addFragmentToContainer(DoctorReviewsFragment.newInstance(doctorId, doctorName, patientServiceId), R.id.fragContainerReview);
    }

    public void replaceToUserReviewsFragment(String serviceId) {
        UserReviewsFragment userReviewsFragment = UserReviewsFragment.newInstance(serviceId);
        fm.beginTransaction().replace(R.id.fragContainerReview, userReviewsFragment).commit();
    }

    public void replaceToUserCommentsFragment(String serviceId) {
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

    public void replaceToHospitalFragment(District district) {
        HospitalFragment hospitalFragment = HospitalFragment.newInstance(district);
        fm.beginTransaction().replace(R.id.fragContainerReview, hospitalFragment).addToBackStack("district_fragment").commit();
    }

    public void replaceToSpecialityFragment(String hospitalId, String patientServiceId) {
        SpecialityFragment specialityFragment = SpecialityFragment.newInstance(hospitalId, patientServiceId);
        fm.beginTransaction().replace(R.id.fragContainerReview, specialityFragment).addToBackStack("ReviewActivityMain").commit();
    }

    public void replaceToEditReviewFragment(District district, Speciality speciality, Doctor doctor, Hospital hospital, String patientServiceId) {
        EditReviewFragment editReviewFragment = EditReviewFragment.newInstance(district, speciality, doctor, hospital, patientServiceId);
        fm.beginTransaction().replace(R.id.fragContainerReview, editReviewFragment).addToBackStack("doctor_reviews_fragment").commit();
    }

    public void replaceToEditReviewFragment(Review review, String patientServiceId) {
        EditReviewFragment editReviewFragment = EditReviewFragment.newInstance(patientServiceId, review);
        fm.beginTransaction().replace(R.id.fragContainerReview, editReviewFragment).addToBackStack("doctor_reviews_fragment").commit();
    }

    public void replaceToDoctorReviewsFragment(String doctorId, String doctorName, String patientServiceId) {
        DoctorReviewsFragment fragment = DoctorReviewsFragment.newInstance(doctorId, doctorName, patientServiceId);
        fm.beginTransaction().replace(R.id.fragContainerReview, fragment).addToBackStack("my_reviews_fragment").commit();
    }

    public void replaceToDoctorReviewDetailedFragment(int reviewId, String patientServiceId) {
        DoctorReviewDetailedFragment fragment = DoctorReviewDetailedFragment.newInstance(reviewId, patientServiceId);
        fm.beginTransaction().replace(R.id.fragContainerReview, fragment).addToBackStack("review_full_fragment").commit();
    }

    public void replaceToDoctorFragment(String hospitalId, String patientServiceId, String specialityId) {
        DoctorFragment doctorFragment = DoctorFragment.newInstance(hospitalId, patientServiceId, specialityId);
        fm.beginTransaction().replace(R.id.fragContainerReview, doctorFragment).addToBackStack("spec_fragment").commit();
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
    public void onDoctorReviewsFragmentBtnClickListener(View v, ReviewsResponse review) {
        switch (v.getId()) {
            case R.id.btnAddReview:
                presenter.onBtnAddReviewClick();
                break;
            case R.id.imBtnComments:
                presenter.onImBtnCommentsClick(review);
                break;
        }
    }

    @Override
    public void onDoctorReviewsFragmentItemClick(ReviewsResponse review) {
        presenter.onDoctorReviewsFragmentItemClick(review);
    }

    @Override
    public void onDoctorReviewDetailedFragmentBtnClick(View v, ReviewsResponse rev) {
        switch (v.getId()) {
            case R.id.imBtnEditReview:
                presenter.onImBtnEditReviewClick(rev);
                break;
        }
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

    @Override
    public void onUserReviewsFragmentBtnClick(View v, Review review) {
        switch (v.getId()) {
            case R.id.imBtnEditReview:
                presenter.onImBtnEditReviewClick(review);
                break;
        }
    }

    @Override
    public void onUserReviewsFragmentDataListener(Review review) {
        presenter.onGetUserReviewsFragmentData(review);
    }

    @Override
    public void onUserCommentsFragmentDataListener(int reviewId) {
        presenter.onGetUserCommentsFragmentData(reviewId);
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
