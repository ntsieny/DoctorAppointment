package com.bigblackboy.doctorappointment;

import android.content.SharedPreferences;

import com.bigblackboy.doctorappointment.model.District;
import com.bigblackboy.doctorappointment.model.Hospital;
import com.bigblackboy.doctorappointment.model.Patient;

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
    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;
    private boolean userLoggedIn;
    private boolean guestMode;

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
}
