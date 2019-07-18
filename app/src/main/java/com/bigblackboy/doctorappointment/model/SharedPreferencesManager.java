package com.bigblackboy.doctorappointment.model;

import android.content.SharedPreferences;

import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;

public class SharedPreferencesManager {

    public static final String APP_SETTINGS = "app_settings";
    public static final String APP_SETTINGS_USER_LOGGED_IN = "user_logged_in";
    public static final String APP_SETTINGS_GUEST_MODE = "guest_mode";
    public static final String APP_SETTINGS_DISTRICT_ID = "district_id";
    public static final String APP_SETTINGS_DISTRICT_NAME = "district_name";
    public static final String APP_SETTINGS_HOSPITAL_ID = "hospital_id";
    public static final String APP_SETTINGS_HOSPITAL_NAME_SHORT = "hospital_name_short";
    public static final String APP_SETTINGS_HOSPITAL_NAME_FULL = "hospital_name_full";
    public static final String APP_SETTINGS_PATIENT_ID = "patient_id";
    public static final String APP_SETTINGS_PATIENT_NAME = "patient_name";
    public static final String APP_SETTINGS_PATIENT_LASTNAME = "patient_lastname";
    public static final String APP_SETTINGS_PATIENT_MIDDLENAME = "patient_middlename";
    public static final String APP_SETTINGS_PATIENT_DAYBIRTH = "patient_daybirth";
    public static final String APP_SETTINGS_PATIENT_MONTHBIRTH = "patient_monthbirth";
    public static final String APP_SETTINGS_PATIENT_YEARBIRTH = "patient_yearbirth";
    public static final String APP_SETTINGS_DOCTOR_ID = "doctor_id";
    public static final String APP_SETTINGS_DOCTOR_NAME = "doctor_name";
    public static final String APP_SETTINGS_SPECIALITY_ID = "speciality_id";
    public static final String APP_SETTINGS_SPECIALITY_NAME = "speciality_name";
    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;

    public SharedPreferencesManager(SharedPreferences mSettings) {
        this.mSettings = mSettings;
    }

    public boolean isUserLoggedIn() {
        return mSettings.getBoolean(APP_SETTINGS_USER_LOGGED_IN, false);
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        editor = mSettings.edit();
        editor.putBoolean(APP_SETTINGS_USER_LOGGED_IN, userLoggedIn);
        editor.apply();
    }

    public boolean isGuestMode() {
        return mSettings.getBoolean(APP_SETTINGS_GUEST_MODE, false);
    }

    public void setGuestMode(boolean guestMode) {
        editor = mSettings.edit();
        editor.putBoolean(APP_SETTINGS_GUEST_MODE, guestMode);
        editor.apply();
    }

    public Patient getCurrentPatient() {
        String patientId = mSettings.getString(APP_SETTINGS_PATIENT_ID, null);
        String name = mSettings.getString(APP_SETTINGS_PATIENT_NAME, "Гость");
        String lastname = mSettings.getString(APP_SETTINGS_PATIENT_LASTNAME, "");
        String middleName = mSettings.getString(APP_SETTINGS_PATIENT_MIDDLENAME, "");
        int dayBirth = mSettings.getInt(APP_SETTINGS_PATIENT_DAYBIRTH, 0);
        int monthBirth = mSettings.getInt(APP_SETTINGS_PATIENT_MONTHBIRTH, 0);
        int yearBirth = mSettings.getInt(APP_SETTINGS_PATIENT_YEARBIRTH, 0);
        Patient patient = new Patient();
        patient.setServiceId(patientId);
        patient.setName(name);
        patient.setLastName(lastname);
        patient.setMiddleName(middleName);
        patient.setDayBirth(dayBirth);
        patient.setMonthBirth(monthBirth);
        patient.setYearBirth(yearBirth);

        String districtId = mSettings.getString(APP_SETTINGS_DISTRICT_ID, null);
        String districtName = mSettings.getString(APP_SETTINGS_DISTRICT_NAME, null);
        District district = new District();
        district.setId(districtId);
        district.setName(districtName);
        patient.setDistrict(district);

        int hospitalId = mSettings.getInt(APP_SETTINGS_HOSPITAL_ID, -1);
        String hospitalNameShort = mSettings.getString(APP_SETTINGS_HOSPITAL_NAME_SHORT, null);
        String hospitalNameFull = mSettings.getString(APP_SETTINGS_HOSPITAL_NAME_FULL, null);
        Hospital hospital = new Hospital();
        hospital.setIdLPU(hospitalId);
        hospital.setLPUShortName(hospitalNameShort);
        hospital.setLpuName(hospitalNameFull);
        patient.setHospital(hospital);

        return patient;
    }

    public District getCurrentDistrict() {
        String districtId = mSettings.getString(APP_SETTINGS_DISTRICT_ID, null);
        String districtName = mSettings.getString(APP_SETTINGS_DISTRICT_NAME, null);
        District district = new District();
        district.setId(districtId);
        district.setName(districtName);
        return district;
    }

    public void setCurrentDistrict(District district) {
        editor = mSettings.edit();
        editor.putString(APP_SETTINGS_DISTRICT_ID, district.getId());
        editor.putString(APP_SETTINGS_DISTRICT_NAME, district.getName());
        editor.apply();
    }

    public Hospital getCurrentHospital() {
        int hospitalId = mSettings.getInt(APP_SETTINGS_HOSPITAL_ID, -1);
        String hospitalNameShort = mSettings.getString(APP_SETTINGS_HOSPITAL_NAME_SHORT, null);
        String hospitalNameFull = mSettings.getString(APP_SETTINGS_HOSPITAL_NAME_FULL, null);
        Hospital hospital = new Hospital();
        hospital.setIdLPU(hospitalId);
        hospital.setLPUShortName(hospitalNameShort);
        hospital.setLpuName(hospitalNameFull);
        return hospital;
    }

    public void setCurrentHospital(Hospital hospital) {
        editor = mSettings.edit();
        editor.putInt(APP_SETTINGS_HOSPITAL_ID, hospital.getIdLPU());
        editor.putString(APP_SETTINGS_HOSPITAL_NAME_SHORT, hospital.getLPUShortName());
        editor.putString(APP_SETTINGS_HOSPITAL_NAME_FULL, hospital.getLpuName());
        editor.apply();
    }

    public void setCurrentDoctor(Doctor doctor) {
        editor = mSettings.edit();
        editor.putString(APP_SETTINGS_DOCTOR_ID, doctor.getIdDoc());
        editor.putString(APP_SETTINGS_DOCTOR_NAME, doctor.getName());
        editor.apply();
    }

    public Doctor getCurrentDoctor() {
        String id = mSettings.getString(APP_SETTINGS_DOCTOR_ID, null);
        String name = mSettings.getString(APP_SETTINGS_DOCTOR_NAME, null);
        return new Doctor(id, name);
    }

    public void setCurrentSpeciality(Speciality speciality) {
        editor = mSettings.edit();
        editor.putString(APP_SETTINGS_SPECIALITY_ID, speciality.getIdSpeciality());
        editor.putString(APP_SETTINGS_SPECIALITY_NAME, speciality.getNameSpeciality());
        editor.apply();
    }

    public Speciality getCurrentSpeciality() {
        String id = mSettings.getString(APP_SETTINGS_SPECIALITY_ID, null);
        String name = mSettings.getString(APP_SETTINGS_SPECIALITY_NAME, null);
        return new Speciality(id, name);
    }

    public void clearSettings() {
        editor = mSettings.edit();
        editor.clear().apply();
    }
}
