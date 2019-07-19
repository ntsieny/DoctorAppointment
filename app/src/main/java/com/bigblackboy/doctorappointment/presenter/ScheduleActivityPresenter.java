package com.bigblackboy.doctorappointment.presenter;

import android.content.SharedPreferences;

import com.bigblackboy.doctorappointment.model.DistrictModel;
import com.bigblackboy.doctorappointment.model.DoctorModel;
import com.bigblackboy.doctorappointment.model.HospitalModel;
import com.bigblackboy.doctorappointment.model.PatientModel;
import com.bigblackboy.doctorappointment.model.SpecialityModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;
import com.bigblackboy.doctorappointment.view.activity.ScheduleActivity;

public class ScheduleActivityPresenter {

    private boolean useSharedPrefsPatientInfo = true;
    private ScheduleActivity view;
    private PatientModel patientModel;
    private HospitalModel hospitalModel;
    private DistrictModel districtModel;
    private SpecialityModel specialityModel;
    private DoctorModel doctorModel;

    public ScheduleActivityPresenter(SharedPreferences prefs) {
        patientModel = new PatientModel(prefs);
        hospitalModel = new HospitalModel();
        districtModel = new DistrictModel();
        specialityModel = new SpecialityModel(prefs);
        doctorModel = new DoctorModel(prefs);
    }

    public void attachView(ScheduleActivity view) {
        this.view = view;
    }

    public void viewIsReady() {

    }

    public void detachView() {
        view = null;
    }

    public void onBtnMyDoctorReviewMainClick() {
        useSharedPrefsPatientInfo = true;

        Patient patient = patientModel.getCurrentPatient();
        String hospitalId;
        if (useSharedPrefsPatientInfo) {
            hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        } else hospitalId = String.valueOf(hospitalModel.getHospital().getIdLPU());

        view.replaceToSpecialityFragment(hospitalId, patient.getServiceId());
    }

    public void onBtnChangeHospitalReviewMainClick() {
        useSharedPrefsPatientInfo = false;
        view.replaceToDistrictFragment();
    }

    public void onGetDistrictFragmentData(District district) {
        districtModel.setDistrict(district);
        view.replaceToHospitalFragment(district);
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

    public void onGetHospitalFragmentData(Hospital hospital) {
        hospitalModel.setHospital(hospital);

        Patient patient = patientModel.getCurrentPatient();
        String hospitalId;
        if (useSharedPrefsPatientInfo) {
            hospitalId = String.valueOf(patient.getHospital().getIdLPU());
        } else hospitalId = String.valueOf(hospitalModel.getHospital().getIdLPU());

        view.replaceToSpecialityFragment(hospitalId, patient.getServiceId());
    }

    public void onGetDoctorFragmentData(Doctor doctor) {
        doctorModel.setDoctor(doctor);

        Patient patient = patientModel.getCurrentPatient();
        if (useSharedPrefsPatientInfo) {
            view.replaceToScheduleFragment(doctor, String.valueOf(patient.getHospital().getIdLPU()));
        } else view.replaceToScheduleFragment(doctor, String.valueOf(hospitalModel.getHospital().getIdLPU()));
    }
}
