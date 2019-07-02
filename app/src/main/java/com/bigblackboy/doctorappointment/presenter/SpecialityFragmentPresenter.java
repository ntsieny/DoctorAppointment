package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.model.SpecialityModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;
import com.bigblackboy.doctorappointment.view.fragment.SpecialityFragment;

import java.util.List;

public class SpecialityFragmentPresenter implements MVPBaseInterface.Presenter {

    private SpecialityModel model;
    private MVPBaseInterface.View view;

    public SpecialityFragmentPresenter(SpecialityModel model) {
        this.model = model;
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

    public void getSpecialities(String hospitalId, String historyId, String patientAriaNumber, String patientId) {
        model.getSpecialities(hospitalId, historyId, patientAriaNumber, patientId, new SpecialityModel.OnFinishedListener() {
            @Override
            public void onFinished(List<Speciality> specialities) {
                ((SpecialityFragment) view).showSpecialities(specialities);
                view.hideProgressBar();
            }

            @Override
            public void onFailure(Throwable t) {
                ((SpecialityFragment) view).popBackStack();
                view.showToast(t.getMessage());
            }
        });
    }
}
