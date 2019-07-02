package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.model.HospitalModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.view.fragment.HospitalFragment;

import java.util.List;

public class HospitalFragmentPresenter implements MVPBaseInterface.Presenter {

    private HospitalModel model;
    private MVPBaseInterface.View view;

    public HospitalFragmentPresenter(HospitalModel model) {
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

    public void getHospitals(String districtId) {
        model.getHospitals(districtId, new HospitalModel.OnFinishedListener() {
            @Override
            public void onFinished(List<Hospital> hospitals) {
                ((HospitalFragment) view).showHospitals(hospitals);
                view.hideProgressBar();
            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }
}
