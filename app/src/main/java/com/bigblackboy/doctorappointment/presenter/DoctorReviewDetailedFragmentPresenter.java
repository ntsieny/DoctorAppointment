package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.model.CommentModel;
import com.bigblackboy.doctorappointment.model.OnFinishedListener;
import com.bigblackboy.doctorappointment.model.ReviewModel;
import com.bigblackboy.doctorappointment.pojos.springpojos.Comment;
import com.bigblackboy.doctorappointment.pojos.springpojos.CommentResponse;
import com.bigblackboy.doctorappointment.pojos.springpojos.ReviewsResponse;
import com.bigblackboy.doctorappointment.view.fragment.DoctorReviewDetailedFragment;

import java.util.Collections;
import java.util.List;

public class DoctorReviewDetailedFragmentPresenter implements MVPBaseInterface.Presenter {

    private MVPBaseInterface.View view;
    private CommentModel commentModel;
    private ReviewModel reviewModel;

    public DoctorReviewDetailedFragmentPresenter() {
        commentModel = new CommentModel();
        reviewModel = new ReviewModel();
    }

    @Override
    public void attachView(MVPBaseInterface.View view) {
        this.view = view;
    }

    @Override
    public void viewIsReady() {
        ((DoctorReviewDetailedFragment) view).hideInnerLayout();
        view.showProgressBar();
    }

    @Override
    public void detachView() {
        view = null;
    }

    public void getComments(int reviewId) {
        commentModel.getComments(reviewId, new CommentModel.OnFinishedListener() {
            @Override
            public void onFinished(List<CommentResponse> comments) {
                view.hideProgressBar();
                Collections.sort(comments);
                ((DoctorReviewDetailedFragment) view).showComments(comments);
                ((DoctorReviewDetailedFragment) view).showInnerLayout();
            }

            @Override
            public void onFailure(Throwable t) {
                view.hideProgressBar();
                ((DoctorReviewDetailedFragment) view).showInnerLayout();
                ((DoctorReviewDetailedFragment) view).setCommentLabelText("Комментариев нет");
            }
        });
    }

    public void sendComment(Comment comment) {
        commentModel.sendComment(comment, new OnFinishedListener() {
            @Override
            public void onFinished() {
                view.showToast("Комментарий отправлен");
                ((DoctorReviewDetailedFragment) view).setEtCommentText("");
                ((DoctorReviewDetailedFragment) view).updateCommentList();
            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    public void sendCommentLike(String serviceId, int commentId) {
        commentModel.sendCommentLike(serviceId, commentId, new OnFinishedListener() {
            @Override
            public void onFinished() {

            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    public void deleteCommentLike(String serviceId, int commentId) {
        commentModel.deleteCommentLike(serviceId, commentId, new OnFinishedListener() {
            @Override
            public void onFinished() {

            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    public void sendCommentDislike(String serviceId, int commentId) {
        commentModel.sendCommentDislike(serviceId, commentId, new OnFinishedListener() {
            @Override
            public void onFinished() {

            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    public void deleteComment(int commentId) {
        commentModel.deleteComment(commentId, new OnFinishedListener() {
            @Override
            public void onFinished() {

            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    public void getReviewById(int reviewId) {
        reviewModel.getReviewById(reviewId, new ReviewModel.OnGetReviewListener() {
            @Override
            public void onFinished(ReviewsResponse review) {
                ((DoctorReviewDetailedFragment) view).showReview(review);
                ((DoctorReviewDetailedFragment) view).setDoctorName(review.getDoctorName());
            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    public void sendReviewLike(String serviceId, int reviewId) {
        reviewModel.sendReviewLike(serviceId, reviewId, new OnFinishedListener() {
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
        reviewModel.sendReviewDislike(serviceId, reviewId, new OnFinishedListener() {
            @Override
            public void onFinished() {

            }

            @Override
            public void onFailure(Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    public void deleteReviewLike(String serviceId, int reviewId) {
        reviewModel.deleteReviewLike(serviceId, reviewId, new OnFinishedListener() {
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
