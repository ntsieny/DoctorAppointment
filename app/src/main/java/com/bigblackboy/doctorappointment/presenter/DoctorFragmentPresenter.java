package com.bigblackboy.doctorappointment.presenter;

import android.content.SharedPreferences;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.model.DoctorModel;
import com.bigblackboy.doctorappointment.model.SpecialityModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.view.fragment.DoctorFragment;

import java.util.List;

public class DoctorFragmentPresenter implements MVPBaseInterface.Presenter {

    private DoctorModel doctorModel;
    private SpecialityModel specialityModel;
    private MVPBaseInterface.View view;

    public DoctorFragmentPresenter(SharedPreferences prefs) {
        doctorModel = new DoctorModel();
        specialityModel = new SpecialityModel(prefs);
    }

    @Override
    public void attachView(MVPBaseInterface.View view) {
        this.view = view;
    }

    @Override
    public void viewIsReady() {
        view.showProgressBar();
    }

    @Override
    public void detachView() {
        view = null;
    }

    public void getDoctors(String specialityId, String hospitalId, String patientId, String historyId) {
        doctorModel.getDoctors(specialityId, hospitalId, patientId, historyId, new DoctorModel.OnFinishedListener() {
            @Override
            public void onFinished(List<Doctor> doctors) {
                ((DoctorFragment) view).showDoctors(doctors);
                view.hideProgressBar();
            }

            @Override
            public void onFailure(Throwable t) {
                ((DoctorFragment) view).popBackStack();
                view.showToast(t.getMessage());
            }
        });
    }

    public void onDoctorReviewsPopupItemClick(Doctor doctor) {
        ((DoctorFragment) view).startReviewActivity(doctor, specialityModel.getChosenSpeciality());
    }
}
