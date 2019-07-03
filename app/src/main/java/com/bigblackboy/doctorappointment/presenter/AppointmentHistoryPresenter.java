package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.model.AppointmentModel;
import com.bigblackboy.doctorappointment.model.OnFinishedListener;
import com.bigblackboy.doctorappointment.pojos.springpojos.Appointment;
import com.bigblackboy.doctorappointment.view.fragment.AppointmentHistoryFragment;

import java.util.Collections;
import java.util.List;

public class AppointmentHistoryPresenter implements MVPBaseInterface.Presenter {

    private MVPBaseInterface.View view;
    private AppointmentModel model;

    public AppointmentHistoryPresenter() {
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

    public void getUserAppointments(String serviceId) {
        model.getAppointments(serviceId, new AppointmentModel.OnFinishedListener() {
            @Override
            public void onFinished(List<Appointment> appointments) {
                Collections.sort(appointments);
                Collections.reverse(appointments);
                ((AppointmentHistoryFragment) view).showAppointments(appointments);
                view.hideProgressBar();
            }

            @Override
            public void onFailure(Throwable t) {
                view.hideProgressBar();
                view.showToast(t.getMessage());
            }
        });
    }

    public void deleteAppointment(int appointmentId) {
        model.deleteAppointment(appointmentId, new OnFinishedListener() {
            @Override
            public void onFinished() {
                view.showToast("Запись отменена");
            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }
}
