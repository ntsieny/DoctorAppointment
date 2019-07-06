package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.model.OnFinishedListener;
import com.bigblackboy.doctorappointment.model.ReviewModel;
import com.bigblackboy.doctorappointment.pojos.springpojos.ReviewsResponse;
import com.bigblackboy.doctorappointment.view.fragment.DoctorReviewsFragment;

import java.util.List;

public class DoctorReviewsFragmentPresenter implements MVPBaseInterface.Presenter {

    private ReviewModel model;
    private MVPBaseInterface.View view;

    public DoctorReviewsFragmentPresenter() {
        model = new ReviewModel();
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

    public void getDoctorReviews(int doctorId) {
        model.getDoctorReviews(doctorId, new ReviewModel.OnGetDoctorReviewsListener() {
            @Override
            public void onFinished(List<ReviewsResponse> reviews) {
                ((DoctorReviewsFragment) view).showDoctorReviews(reviews);
                float avgMark = countAvgMark(reviews);
                ((DoctorReviewsFragment) view).setDoctorStarRating(avgMark);
                ((DoctorReviewsFragment) view).setDoctorTextRating(avgMark);
                view.hideProgressBar();
                ((DoctorReviewsFragment) view).showInnerLayout();
            }

            @Override
            public void onFailure(Throwable t) {
                view.hideProgressBar();
                ((DoctorReviewsFragment) view).showInnerLayout();
                ((DoctorReviewsFragment) view).showReviewsEmptyLabel();
            }
        });
    }

    public void sendReviewLike(String serviceId, int reviewId) {
        model.sendReviewLike(serviceId, reviewId, new OnFinishedListener() {
            @Override
            public void onFinished() {

            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    public void sendReviewDislike(String serviceId, int reviewId) {
        model.sendReviewDislike(serviceId, reviewId, new OnFinishedListener() {
            @Override
            public void onFinished() {

            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    public void deleteLike(String serviceId, int reviewId) {
        model.deleteReviewLike(serviceId, reviewId, new OnFinishedListener() {
            @Override
            public void onFinished() {

            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    private float countAvgMark(List<ReviewsResponse> list) {
        float sum = 0;
        for (ReviewsResponse rev : list) {
            sum += rev.getMark();
        }
        return sum / list.size();
    }
}
