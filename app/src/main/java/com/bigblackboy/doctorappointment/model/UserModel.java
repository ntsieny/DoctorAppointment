package com.bigblackboy.doctorappointment.model;

import android.content.SharedPreferences;
import android.util.Log;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.api.CheckPatientApiResponse;
import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.bigblackboy.doctorappointment.controller.HospitalController;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.pojos.springpojos.Response;
import com.bigblackboy.doctorappointment.pojos.springpojos.User;
import com.bigblackboy.doctorappointment.utils.MyApplication;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_DISTRICT_ID;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_DISTRICT_NAME;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_ID;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_NAME_FULL;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_NAME_SHORT;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_DAYBIRTH;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_ID;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_LASTNAME;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_MIDDLENAME;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_MONTHBIRTH;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_NAME;
import static com.bigblackboy.doctorappointment.model.SharedPreferencesManager.APP_SETTINGS_PATIENT_YEARBIRTH;

public class UserModel {

    private static final String LOG_TAG = "myLog: UserModel";
    private SharedPreferences mSettings;
    private SharedPreferencesManager prefManager;
    private SharedPreferences.Editor editor;
    private SpringApi springApi;
    private HospitalApi hospitalApi;
    private User user;

    public UserModel() {
        springApi = SpringController.getApi();
        hospitalApi = HospitalController.getApi();
        user = new User();
    }

    public UserModel(SharedPreferences mSettings) {
        this.mSettings = mSettings;
        prefManager = new SharedPreferencesManager(mSettings);
        springApi = SpringController.getApi();
        hospitalApi = HospitalController.getApi();
        user = new User();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUserLoggedIn(boolean value) {
        prefManager.setUserLoggedIn(value);
    }

    public boolean isUserLoggedIn() {
        return prefManager.isUserLoggedIn();
    }

    public boolean isGuestMode() {
        return prefManager.isGuestMode();
    }

    public void setGuestMode(boolean value) {
        prefManager.setGuestMode(value);
    }

    public void setDistrict(District district) {
        user.setDistrictId(Integer.valueOf(district.getId()));
        user.setDistrictName(district.getName());
        prefManager.setCurrentDistrict(district);
    }

    public District getDistrict() {
        return prefManager.getCurrentDistrict();
    }

    public void setHospital(Hospital hospital) {
        user.setHospitalId(hospital.getIdLPU());
        user.setLpuNameShort(hospital.getLPUShortName());
        user.setLpuNameFull(hospital.getLpuName());
        user.setLpuAddress(hospital.getFullAddress());
        user.setLpuEmail(hospital.getEmail());
        user.setLpuType(hospital.getLpuType());
        user.setLpuWorkTime(hospital.getWorkTime());
        prefManager.setCurrentHospital(hospital);
    }

    public Hospital getHospital() {
        return prefManager.getCurrentHospital();
    }

    public void clearSettings() {
        prefManager.clearSettings();
    }

    private void writeSharedPreferences(User user) {
        editor = mSettings.edit();
        editor.putString(APP_SETTINGS_PATIENT_ID, user.getServiceId());
        editor.putString(APP_SETTINGS_PATIENT_NAME, user.getName());
        editor.putString(APP_SETTINGS_PATIENT_LASTNAME, user.getLastname());
        editor.putString(APP_SETTINGS_PATIENT_MIDDLENAME, user.getMiddlename());
        editor.putInt(APP_SETTINGS_PATIENT_DAYBIRTH, user.getDayBirth());
        editor.putInt(APP_SETTINGS_PATIENT_MONTHBIRTH, user.getMonthBirth());
        editor.putInt(APP_SETTINGS_PATIENT_YEARBIRTH, user.getYearBirth());
        editor.putString(APP_SETTINGS_DISTRICT_ID, String.valueOf(user.getDistrictId()));
        editor.putString(APP_SETTINGS_DISTRICT_NAME, user.getDistrictName());
        editor.putInt(APP_SETTINGS_HOSPITAL_ID, user.getHospitalId());
        editor.putString(APP_SETTINGS_HOSPITAL_NAME_SHORT, user.getLpuNameShort());
        editor.putString(APP_SETTINGS_HOSPITAL_NAME_FULL, user.getLpuNameFull());
        editor.apply();
    }

    public void getUserByLoginPassword(String login, String password, final OnFinishedListener onFinishedListener) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        springApi.getUserByLoginPassword(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                User user = response.body();
                if (response.isSuccessful() && user != null) {
                    writeSharedPreferences(user);
                    setUserLoggedIn(true);
                    onFinishedListener.onFinished(user);
                } else onFinishedListener.onFailure(new Throwable(MyApplication.getAppContext().getResources().getString(R.string.user_not_found)));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    public void createUser(final User user, final OnFinishedListener onFinishedListener) {
        springApi.createUser(user).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response resp = response.body();
                if (response.isSuccessful()) {
                    if (resp.isSuccess()) {
                        writeSharedPreferences(user);
                        setUserLoggedIn(true);
                        onFinishedListener.onFinished(user);
                    } else onFinishedListener.onFailure(new Throwable(MyApplication.getAppContext().getResources().getString(R.string.user_not_created)));
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        onFinishedListener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    public void checkUserExistsInHospital(final Patient patient, String token, final OnCheckUserListener listener) {
        hospitalApi.getMetadata(patient.getName(), patient.getLastName(), patient.getMiddleName(), patient.getInsuranceSeries(), patient.getInsuranceNumber(),
                patient.getBirthdayFormatted(), String.valueOf(patient.getHospital().getIdLPU()), "").enqueue(new Callback<CheckPatientApiResponse>() {
            @Override
            public void onResponse(Call<CheckPatientApiResponse> call, retrofit2.Response<CheckPatientApiResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body().getSuccess()) {
                        CheckPatientApiResponse respObj = response.body();
                        patient.setServiceId(respObj.getResponse().getPatientId());
                        listener.onFinished(patient);
                        //String cookie = response.headers().get("Set-Cookie");
                    } else listener.onFailure(new Throwable("Ошибка авторизации: " + response.body().getError().getErrorDescription()));
                } else listener.onFailure(new Throwable("Запрос не прошел (" + response.code() + ")"));
            }

            @Override
            public void onFailure(Call<CheckPatientApiResponse> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public void checkLoginUnique(String login, String serviceId, final OnCheckLoginUnique listener) {
        User user = new User();
        user.setLogin(login);
        user.setServiceId(serviceId);
        springApi.checkLoginUnique(user).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response resp = response.body();
                if (resp.isSuccess())
                    listener.onFinished(true);
                else listener.onFinished(false);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public interface OnFinishedListener {
        void onFinished(User user);
        void onFailure(Throwable t);
    }

    public interface OnCheckUserListener {
        void onFinished(Patient patient);
        void onFailure(Throwable t);
    }

    public interface OnCheckLoginUnique {
        void onFinished(boolean unique);
        void onFailure(Throwable t);
    }
}
