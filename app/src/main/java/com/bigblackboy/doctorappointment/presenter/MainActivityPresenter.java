package com.bigblackboy.doctorappointment.presenter;

import android.content.SharedPreferences;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.AppointmentModel;
import com.bigblackboy.doctorappointment.model.DoctorModel;
import com.bigblackboy.doctorappointment.model.OnFinishedListener;
import com.bigblackboy.doctorappointment.model.PatientModel;
import com.bigblackboy.doctorappointment.model.SpecialityModel;
import com.bigblackboy.doctorappointment.model.UserModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.AppointmentInfo;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;
import com.bigblackboy.doctorappointment.pojos.springpojos.Appointment;
import com.bigblackboy.doctorappointment.view.activity.MainActivity;
import com.bigblackboy.doctorappointment.view.activity.ReviewActivity;

public class MainActivityPresenter {

    private MainActivity view;
    private AppointmentModel appointmentModel;
    private PatientModel patientModel;
    private UserModel userModel;
    private SpecialityModel specialityModel;
    private DoctorModel doctorModel;

    public MainActivityPresenter(SharedPreferences prefs) {
        appointmentModel = new AppointmentModel(prefs);
        patientModel = new PatientModel(prefs);
        userModel = new UserModel(prefs);
        specialityModel = new SpecialityModel(prefs);
        doctorModel = new DoctorModel(prefs);
    }

    public void attachView(MainActivity view) {
        this.view = view;
    }

    public void viewIsReady() {

    }

    public void detachView() {
        view = null;
    }

    public void onCreateActivity() {
        Patient patient = patientModel.getCurrentPatient();
        if (userModel.isUserLoggedIn()) {
            view.setNavigationDrawer();
            view.replaceToProfileFragment(patient);

            String fio = String.format("%s %s %s", patient.getLastName(), patient.getName(), patient.getMiddleName());
            view.setPatientFIO(fio);
            String address = String.format("%s, %s", patient.getHospital().getLPUShortName(), patient.getDistrict().getName());
            view.setPatientAddress(address);
        } else if (userModel.isGuestMode()) {
            view.setNavigationDrawer();
            view.replaceToProfileFragment(patient);
            view.setPatientFIO(patient.getName());
            String address = String.format("%s, %s", patient.getHospital().getLPUShortName(), patient.getDistrict().getName());
            view.setPatientAddress(address);
        } else {
            view.addDistrictFragment();
            view.setNavigationDrawer();
        }
    }

    public void onProfileItemClick() {
        view.replaceToProfileFragment(patientModel.getCurrentPatient());
    }

    public void onMakeAppointmentItemClick() {
        if (userModel.isUserLoggedIn()) {
            view.replaceToSpecialityFragment(patientModel.getCurrentPatient());
        } else view.showToast(R.string.toast_you_have_to_login_for_action);
    }

    public void onDoctorScheduleItemClick() {
        view.startScheduleActivity();
    }

    public void onAppointmentHistoryItemClick() {
        if (userModel.isUserLoggedIn()) {
            view.replaceToAppointmentHistoryFragment(patientModel.getCurrentPatient().getServiceId());
        } else view.showToast(R.string.toast_you_have_to_login_for_action);
    }

    public void onReviewsItemClick() {
        view.startReviewActivity(ReviewActivity.FRAGMENT_MAIN_MENU);
    }

    public void onCheckupItemClick() {
        if (userModel.isUserLoggedIn()) {
            view.replaceToCheckupFragment(patientModel.getAge());
        } else if (userModel.isGuestMode()) {
            view.replaceToCheckupFragment(20);
        }
    }

    public void onLogoutItemClick() {
        userModel.clearSettings();
        view.startChooseLoginActivity();
    }

    public void onGetDistrictFragmentData(District district) {
        Patient patient = patientModel.getCurrentPatient();
        patient.setDistrict(district);
        view.replaceToHospitalFragment(district);
        userModel.setDistrict(district);
    }

    public void onGetHospitalFragmentData(Hospital hospital) {
        Patient patient = patientModel.getCurrentPatient();
        patient.setHospital(hospital);
        view.popBackStack();
        view.replaceToProfileFragment(patient);
        if (!userModel.isUserLoggedIn()) {
            userModel.setGuestMode(true);
            userModel.setHospital(hospital);
        }
        view.setPatientFIO("Гость");
        String address = String.format("%s, %s", patient.getHospital().getLPUShortName(), patient.getDistrict().getName());
        view.setPatientAddress(address);
    }

    public void onGetSpecialityFragmentData(Speciality speciality) {
        specialityModel.saveSpeciality(speciality);
        Patient patient = patientModel.getCurrentPatient();
        view.replaceToDoctorFragment(String.valueOf(patient.getHospital().getIdLPU()), patient.getServiceId(), speciality.getIdSpeciality());
    }

    public void onGetDoctorFragmentData(Doctor doctor) {
        doctorModel.saveDoctor(doctor);
        Patient patient = patientModel.getCurrentPatient();
        view.replaceToChooseAppointmentFragment(doctor.getIdDoc(), String.valueOf(patient.getHospital().getIdLPU()), patient.getServiceId());
    }


    public void onGetAppointmentFragmentData(AppointmentInfo appInfo) {
        Patient patient = patientModel.getCurrentPatient();

        Appointment app = new Appointment();
        app.setAppString(appInfo.getId());
        app.setServiceId(patient.getServiceId());
        app.setDistrictId(Integer.valueOf(patient.getDistrict().getId()));
        app.setDistrictName(patient.getDistrict().getName());
        app.setLpuId(patient.getHospital().getIdLPU());
        app.setLpuNameShort(patient.getHospital().getLPUShortName());
        app.setLpuNameFull(patient.getHospital().getLpuName());
        app.setSpecId(Integer.valueOf(specialityModel.getSavedSpeciality().getIdSpeciality()));
        app.setSpecName(specialityModel.getSavedSpeciality().getNameSpeciality());
        app.setDocId(Integer.valueOf(doctorModel.getSavedDoctor().getIdDoc()));
        app.setDocName(doctorModel.getSavedDoctor().getName());
        app.setDateTime(appInfo.getDateStart().getDateTime());

        view.popBackStack();
        view.replaceToAppointmentHistoryFragment(patient.getServiceId());

        appointmentModel.createAppointment(app, new OnFinishedListener() {
            @Override
            public void onFinished() {
                view.showToast("Запись сделана");
            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    public void onMyAppointmentsBtnClick() {
        if(!userModel.isGuestMode()) {
            view.replaceToAppointmentHistoryFragment(patientModel.getCurrentPatient().getServiceId());
        } else view.showToast(R.string.toast_you_have_to_login_for_action);
    }

    public void onMyReviewsBtnClick() {
        if(!userModel.isGuestMode()) {
            view.startReviewActivity(ReviewActivity.FRAGMENT_MY_REVIEWS);
        } else view.showToast(R.string.toast_you_have_to_login_for_action);
    }

    public void onMyCommentsBtnClick() {
        if(!userModel.isGuestMode()) {
            view.startReviewActivity(ReviewActivity.FRAGMENT_MY_COMMENTS);
        } else view.showToast(R.string.toast_you_have_to_login_for_action);
    }
}
