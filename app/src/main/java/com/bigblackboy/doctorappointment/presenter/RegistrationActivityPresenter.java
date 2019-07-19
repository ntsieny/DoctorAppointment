package com.bigblackboy.doctorappointment.presenter;

import android.content.SharedPreferences;

import com.bigblackboy.doctorappointment.model.UserModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.pojos.springpojos.User;
import com.bigblackboy.doctorappointment.view.activity.RegistrationActivity;

import java.util.Map;

public class RegistrationActivityPresenter {

    private RegistrationActivity view;
    private UserModel userModel;

    public RegistrationActivityPresenter(SharedPreferences prefs) {
        userModel = new UserModel(prefs);
    }

    public void attachView(RegistrationActivity view) {
        this.view = view;
    }

    public void detachView() {
        view = null;
    }

    public void onGetDistrictFragmentData(District district) {
        userModel.setDistrict(district);
        view.replaceToHospitalFragment(district);
    }

    public void onGetHospitalFragmentData(Hospital hospital) {
        userModel.setHospital(hospital);
        view.replaceToSignUpFragment();
    }

    public void onGetSignUpFragmentData(Map<String, String> loginAndPassword) {
        String login = loginAndPassword.get("login");
        String password = loginAndPassword.get("password");
        userModel.getUser().setLogin(login);
        userModel.getUser().setPassword(password);
        view.replaceToInputBioFragment(String.valueOf(userModel.getHospital().getIdLPU()));
    }

    public void onGetInputBioFragmentData(Patient patient) {
        District district = userModel.getDistrict();
        Hospital hospital = userModel.getHospital();

        User user = new User();
        user.setLogin(userModel.getUser().getLogin());
        user.setPassword(userModel.getUser().getPassword());
        user.setPatientId(0);
        user.setDistrictId(Integer.parseInt(district.getId()));
        user.setDistrictName(district.getName());
        user.setHospitalId(hospital.getIdLPU());
        user.setLpuNameShort(hospital.getLPUShortName());
        user.setLpuNameFull(hospital.getLpuName());
        user.setLpuAddress(hospital.getFullAddress());
        user.setLpuEmail(hospital.getEmail());
        user.setLpuType(hospital.getLpuType());
        user.setLpuWorkTime(hospital.getWorkTime());
        user.setName(patient.getName());
        user.setLastname(patient.getLastName());
        user.setMiddlename(patient.getMiddleName());
        user.setDayBirth(patient.getDayBirth());
        user.setMonthBirth(patient.getMonthBirth());
        user.setYearBirth(patient.getYearBirth());
        user.setServiceId(patient.getServiceId());

        userModel.createUser(user, new UserModel.OnFinishedListener() {
            @Override
            public void onFinished(User user) {
                view.openMainMenuActivity();
                view.showToast(String.format("Добро пожаловать, %s!", user.getName()));
            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }
}
