package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.model.AppointmentModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.AppointmentInfo;
import com.bigblackboy.doctorappointment.view.fragment.ChooseAppointmentFragment;

import java.util.Collections;
import java.util.List;

public class ChooseAppointmentPresenter implements MVPBaseInterface.Presenter {

    private AppointmentModel model;
    private MVPBaseInterface.View view;

    public ChooseAppointmentPresenter() {
        model = new AppointmentModel();
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

    public void getAppointmentDates(String doctorId, String hospitalId, String patientId, String historyId, String appointmentType) {
        model.getAppointmentDates(doctorId, hospitalId, patientId, historyId, appointmentType, new AppointmentModel.AppointmentDatesListener() {
            @Override
            public void onFinished(List<AppointmentInfo> appointmentDates) {
                Collections.sort(appointmentDates);
                ((ChooseAppointmentFragment) view).showAppointmentDates(appointmentDates);
                view.hideProgressBar();
            }

            @Override
            public void onFailure(Throwable t) {
                ((ChooseAppointmentFragment) view).popBackStack();
                view.showToast(t.getMessage());
            }
        });
    }
}
