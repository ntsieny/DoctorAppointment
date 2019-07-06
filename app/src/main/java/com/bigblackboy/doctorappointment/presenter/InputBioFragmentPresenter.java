package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.model.UserModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Patient;
import com.bigblackboy.doctorappointment.view.fragment.InputBioFragment;

public class InputBioFragmentPresenter implements MVPBaseInterface.Presenter {

    private UserModel model;
    private MVPBaseInterface.View view;

    public InputBioFragmentPresenter() {
        model = new UserModel();
    }

    @Override
    public void attachView(MVPBaseInterface.View view) {
        this.view = view;
    }

    @Override
    public void viewIsReady() {

    }

    @Override
    public void detachView() {
        view = null;
    }

    public void checkUserExistsInHospital(Patient patient, String token) {
        model.checkUserExistsInHospital(patient, token, new UserModel.OnCheckUserListener() {
            @Override
            public void onFinished(Patient patient) {
                ((InputBioFragment)view).openMainMenuActivity(patient);
            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    public void onChooseDateLabelClick() {
        ((InputBioFragment)view).showDatePicker();
    }

    public void onRegistrationBtnClick() {

    }
}
