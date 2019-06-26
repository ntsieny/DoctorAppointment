package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.model.DistrictModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.view.fragment.DistrictFragment;

import java.util.ArrayList;

public class DistrictFragmentPresenter {

    private DistrictFragment view;
    private DistrictModel districtModel;

    public DistrictFragmentPresenter() {
        districtModel = new DistrictModel();
    }

    public void attachView(DistrictFragment view) {
        this.view = view;
    }

    public void detachView() {
        view = null;
    }

    public void getDistricts() {
        districtModel.getDistricts(new DistrictModel.OnFinishedListener() {
            @Override
            public void onFinished(ArrayList<District> districts) {
                if(districts != null)
                    view.showDistricts(districts);
            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }
}
