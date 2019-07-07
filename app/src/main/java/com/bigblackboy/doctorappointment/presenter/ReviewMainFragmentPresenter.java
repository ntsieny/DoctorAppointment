package com.bigblackboy.doctorappointment.presenter;

import android.content.SharedPreferences;

import com.bigblackboy.doctorappointment.model.SharedPreferencesManager;
import com.bigblackboy.doctorappointment.view.fragment.ReviewMainFragment;

public class ReviewMainFragmentPresenter {

    private SharedPreferencesManager model;
    private ReviewMainFragment view;

    public ReviewMainFragmentPresenter(SharedPreferences mSettings) {
        model = new SharedPreferencesManager(mSettings);
    }

    public void attachView(ReviewMainFragment view) {
        this.view = view;
    }

    public void viewIsReady() {
        view.setHospitalAddress(model.getCurrentHospital().getLPUShortName() + ", " + model.getCurrentDistrict().getName());
    }

    public void detachView() {
        view = null;
    }
}
