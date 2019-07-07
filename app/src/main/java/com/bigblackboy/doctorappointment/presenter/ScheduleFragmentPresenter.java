package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.api.ScheduleApiResponse;
import com.bigblackboy.doctorappointment.model.DoctorModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.view.fragment.ScheduleFragment;

import java.util.List;

public class ScheduleFragmentPresenter {

    private DoctorModel model;
    private ScheduleFragment view;
    private Doctor doctor;

    public ScheduleFragmentPresenter(Doctor doctor) {
        model = new DoctorModel();
        this.doctor = doctor;
    }

    public void attachView(ScheduleFragment view) {
        this.view = view;
    }

    public void viewIsReady() {
        view.setDoctorName(doctor.getName());
    }

    public void detachView() {
        view = null;
    }

    public void getSchedule(String doctorId, String hospitalId, String patientId, String historyId, String appointmentType) {
        model.getSchedule(doctorId, hospitalId, patientId, historyId, appointmentType, new DoctorModel.OnGetScheduleListener() {
            @Override
            public void onFinished(List<ScheduleApiResponse.MResponse> schedule) {
                view.showSchedule(schedule);
                view.hideProgressBar();
                view.showInnerLayout();
            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }
}
