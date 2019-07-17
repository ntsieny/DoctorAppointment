package com.bigblackboy.doctorappointment.model;

import android.content.SharedPreferences;

import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.utils.AgeCalculator;

import org.joda.time.LocalDate;

public class PatientModel {

    private Patient patient;
    private SharedPreferences mSettings;
    private SharedPreferencesManager prefManager;

    public PatientModel(SharedPreferences prefs) {
        mSettings = prefs;
        prefManager = new SharedPreferencesManager(mSettings);
        patient = getCurrentPatient();
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Patient getCurrentPatient() {
        return prefManager.getCurrentPatient();
    }

    public void setDistrict(District district) {
        patient.setDistrict(district);
    }

    public void setHospital(Hospital hospital) {
        patient.setHospital(hospital);
    }

    public int getAge() {
        return AgeCalculator.getAge(new LocalDate(patient.getYearBirth(), patient.getMonthBirth(), patient.getDayBirth()), new LocalDate());
    }


}
