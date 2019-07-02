package com.bigblackboy.doctorappointment;

public interface MVPBaseInterface {

    public interface View {
        public void showToast(int resId);
        public void showToast(String message);
        public void showProgressBar();
        public void hideProgressBar();
    }

    public interface Presenter {
        public void attachView(View view);
        public void viewIsReady();
        public void detachView();
    }
}
