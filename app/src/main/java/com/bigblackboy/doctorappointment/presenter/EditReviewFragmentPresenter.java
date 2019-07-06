package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.model.OnFinishedListener;
import com.bigblackboy.doctorappointment.model.ReviewModel;
import com.bigblackboy.doctorappointment.pojos.springpojos.Review;
import com.bigblackboy.doctorappointment.view.fragment.EditReviewFragment;

public class EditReviewFragmentPresenter implements MVPBaseInterface.Presenter {

    private ReviewModel model;
    private MVPBaseInterface.View view;

    public EditReviewFragmentPresenter() {
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

    public void sendReview(Review review) {
        model.sendReview(review, new OnFinishedListener() {
            @Override
            public void onFinished() {
                ((EditReviewFragment)view).popBackStack();
            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    public void editReview(Review review) {
        model.editReview(review, new OnFinishedListener() {
            @Override
            public void onFinished() {
                view.showToast("Отзыв отредактирован");
                ((EditReviewFragment)view).popBackStack();
            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    public void deleteReview(int reviewId) {
        model.deleteReview(reviewId, new OnFinishedListener() {
            @Override
            public void onFinished() {
                view.showToast("Отзыв удален");
                ((EditReviewFragment)view).popBackStack();
                ((EditReviewFragment)view).popBackStack();
            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }
}
