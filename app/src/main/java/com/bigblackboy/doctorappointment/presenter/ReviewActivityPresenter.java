package com.bigblackboy.doctorappointment.presenter;

import android.content.SharedPreferences;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.DistrictModel;
import com.bigblackboy.doctorappointment.model.DoctorModel;
import com.bigblackboy.doctorappointment.model.HospitalModel;
import com.bigblackboy.doctorappointment.model.PatientModel;
import com.bigblackboy.doctorappointment.model.SpecialityModel;
import com.bigblackboy.doctorappointment.model.UserModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;
import com.bigblackboy.doctorappointment.pojos.springpojos.Review;
import com.bigblackboy.doctorappointment.pojos.springpojos.ReviewsResponse;
import com.bigblackboy.doctorappointment.view.activity.ReviewActivity;

public class ReviewActivityPresenter {

    private ReviewActivity view;
    private PatientModel patientModel;
    private UserModel userModel;
    private SpecialityModel specialityModel;
    private DoctorModel doctorModel;
    private HospitalModel hospitalModel;
    private DistrictModel districtModel;
    private boolean useSharedPrefsPatientInfo = true;


    public ReviewActivityPresenter(SharedPreferences prefs) {
        patientModel = new PatientModel(prefs);
        userModel = new UserModel(prefs);
        specialityModel = new SpecialityModel(prefs);
        doctorModel = new DoctorModel(prefs);
        hospitalModel = new HospitalModel();
        districtModel = new DistrictModel();
    }

    public void attachView(ReviewActivity view) {
        this.view = view;
    }

    public void viewIsReady() {

    }

    public void detachView() {
        view = null;
    }

    public void onMainMenuFragmentLoad() {
        view.addReviewMainFragment();
    }

    public void onMyReviewsFragmentLoad() {
        Patient patient = patientModel.getCurrentPatient();
        view.replaceToUserReviewsFragment(patient.getServiceId());
    }

    public void onMyCommentsFragmentLoad() {
        Patient patient = patientModel.getCurrentPatient();
        view.replaceToUserCommentsFragment(patient.getServiceId());
    }

    public void onDoctorReviewsFragmentLoad(Doctor doctor, Speciality speciality) {
        doctorModel.saveDoctor(doctor);
        specialityModel.saveSpeciality(speciality);
        view.addDoctorReviewsFragment(doctor.getIdDoc(), doctor.getName(), patientModel.getCurrentPatient().getServiceId());
    }

    public void onGetHospitalFragmentData(Hospital hospital) {
        hospitalModel.setHospital(hospital);
        Patient patient = patientModel.getCurrentPatient();
        String hospitalId;
        if (useSharedPrefsPatientInfo) {
            hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        } else hospitalId = String.valueOf(hospital.getIdLPU());
        view.replaceToSpecialityFragment(hospitalId, patient.getServiceId());
    }

    public void onBtnMyDoctorReviewMainClick() {
        Patient patient = patientModel.getCurrentPatient();
        String hospitalId;
        if (useSharedPrefsPatientInfo) {
            hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        } else hospitalId = String.valueOf(hospitalModel.getHospital().getIdLPU());
        view.replaceToSpecialityFragment(hospitalId, patient.getServiceId());
    }

    public void onGetSpecialityFragmentData(Speciality speciality) {
        specialityModel.setSpeciality(speciality);
        Patient patient = patientModel.getCurrentPatient();
        String hospitalId;
        if (useSharedPrefsPatientInfo) {
            hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        } else hospitalId = String.valueOf(hospitalModel.getHospital().getIdLPU());
        view.replaceToDoctorFragment(hospitalId, patient.getServiceId(), speciality.getIdSpeciality());
    }

    public void onGetDoctorFragmentData(Doctor doctor) {
        doctorModel.setDoctor(doctor);
        view.replaceToDoctorReviewsFragment(doctor.getIdDoc(), doctor.getName(), patientModel.getCurrentPatient().getServiceId());
    }

    public void onBtnAddReviewClick() {
        if (!userModel.isGuestMode()) {
            if (useSharedPrefsPatientInfo) {
                view.replaceToEditReviewFragment(userModel.getDistrict(), specialityModel.getSpeciality(), doctorModel.getDoctor(),
                        userModel.getHospital(), patientModel.getCurrentPatient().getServiceId());
            } else {
                view.replaceToEditReviewFragment(districtModel.getDistrict(), specialityModel.getSpeciality(), doctorModel.getDoctor(),
                        hospitalModel.getHospital(), patientModel.getCurrentPatient().getServiceId());
            }
        } else view.showToast(R.string.toast_you_have_to_login_for_action);
    }

    public void onBtnChangeHospitalReviewMainClick() {
        useSharedPrefsPatientInfo = false;
        view.replaceToDistrictFragment();
    }

    public void onImBtnEditReviewClick(Review review) {
        view.replaceToEditReviewFragment(review, patientModel.getCurrentPatient().getServiceId());
    }

    public void onGetDistrictFragmentData(District district) {
        districtModel.setDistrict(district);
        view.replaceToHospitalFragment(district);
    }

    public void onImBtnCommentsClick(ReviewsResponse review) {
        view.replaceToDoctorReviewDetailedFragment(review.getReviewId(), patientModel.getCurrentPatient().getServiceId());
    }

    public void onDoctorReviewsFragmentItemClick(ReviewsResponse review) {
        view.replaceToDoctorReviewDetailedFragment(review.getReviewId(), patientModel.getCurrentPatient().getServiceId());
    }

    public void onGetUserReviewsFragmentData(Review review) {
        view.replaceToDoctorReviewsFragment(String.valueOf(review.getDoctorId()), review.getDoctorName(), patientModel.getCurrentPatient().getServiceId());
    }

    public void onGetUserCommentsFragmentData(int reviewId) {
        view.replaceToDoctorReviewDetailedFragment(reviewId, patientModel.getCurrentPatient().getServiceId());
    }
}
