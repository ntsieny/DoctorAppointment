package com.bigblackboy.doctorappointment.model;

public interface OnFinishedListener {

    void onFinished();

    void onFailure(Throwable t);
}
