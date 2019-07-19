package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.model.OnFinishedListener;
import com.bigblackboy.doctorappointment.model.ReviewModel;
import com.bigblackboy.doctorappointment.pojos.springpojos.Review;
import com.bigblackboy.doctorappointment.view.fragment.UserReviewsFragment;

import java.util.List;

public class UserReviewsFragmentPresenter {

    private UserReviewsFragment view;
    private ReviewModel model;

    public UserReviewsFragmentPresenter() {
        model = new ReviewModel();
    }

    public void attachView(UserReviewsFragment view) {
        this.view = view;
    }

    public void viewIsReady() {
        view.showProgressBar();
    }

    public void detachView() {
        view = null;
    }

    public void getUserReviews(String serviceId) {
        model.getUserReviews(serviceId, new ReviewModel.OnGetUserReviewsListener() {
            @Override
            public void onFinished(List<Review> reviews) {
                view.hideProgressBar();
                if(reviews != null) {
                    view.setReviewsLabel("Мои отзывы");
                    view.showReviews(reviews);
                } else view.setReviewsLabel("Отзывов нет");
            }

            @Override
            public void onFailure(Throwable t) {
                view.hideProgressBar();
                view.showToast(t.getMessage());
            }
        });
    }

    public void deleteReview(int reviewId) {
        model.deleteReview(reviewId, new OnFinishedListener() {
            @Override
            public void onFinished() {

            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }
}
