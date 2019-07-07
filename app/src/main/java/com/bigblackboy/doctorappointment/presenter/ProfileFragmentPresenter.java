package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.utils.AgeCalculator;
import com.bigblackboy.doctorappointment.view.fragment.ProfileFragment;

import org.joda.time.LocalDate;

public class ProfileFragmentPresenter {

    private ProfileFragment view;

    public void attachView(ProfileFragment view) {
        this.view = view;
    }

    public void viewIsReady() {

    }

    public void detachView() {
        view = null;
    }

    public void setPatientInfo(Patient patient) {
        if (patient.getName() != null && patient.getLastName() != null) {
            view.setPatientFio(String.format("%s %s %s", patient.getLastName(), patient.getName(), patient.getMiddleName()));
        } else if (patient.getName() != null && patient.getLastName() == null) {
            view.setPatientFio(patient.getName());
        }

        if(patient.getDayBirth() > 0 && patient.getMonthBirth() > 0 && patient.getYearBirth() > 0) {
            String birthday = String.format("%d.%d.%d", patient.getDayBirth(), patient.getMonthBirth(), patient.getYearBirth());
            String ageVerbal = AgeCalculator.getAgeVerbal(new LocalDate(patient.getYearBirth(), patient.getMonthBirth(), patient.getDayBirth()), new LocalDate());
            view.setPatientBirthday(String.format("%s, %s", birthday, ageVerbal));
        } else view.hideBirthdayLabel();

        if (patient.getDistrict().getName() != null && patient.getHospital().getLPUShortName() != null) {
            view.setDistrict(patient.getDistrict().getName());
            view.setHospital(patient.getHospital().getLPUShortName());
        }
    }
}
