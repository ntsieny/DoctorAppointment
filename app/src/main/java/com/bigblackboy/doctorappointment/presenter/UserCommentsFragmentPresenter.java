package com.bigblackboy.doctorappointment.presenter;

import com.bigblackboy.doctorappointment.model.CommentModel;
import com.bigblackboy.doctorappointment.model.OnFinishedListener;
import com.bigblackboy.doctorappointment.pojos.springpojos.MyCommentsResponse;
import com.bigblackboy.doctorappointment.view.fragment.UserCommentsFragment;

import java.util.List;

public class UserCommentsFragmentPresenter {

    private UserCommentsFragment view;
    private CommentModel model;

    public UserCommentsFragmentPresenter() {
        model = new CommentModel();
    }

    public void attachView(UserCommentsFragment view) {
        this.view = view;
    }

    public void viewIsReady() {
        view.showProgressBar();
    }

    public void detachView() {
        view = null;
    }

    public void getUserComments(String serviceId) {
        model.getUserComments(serviceId, new CommentModel.OnGetUserCommentsListener() {
            @Override
            public void onFinished(List<MyCommentsResponse> comments) {
                view.hideProgressBar();
                if(comments != null) {
                    view.setCommentsLabel("Мои комментарии");
                    view.showComments(comments);
                } else view.setCommentsLabel("Комментариев нет");
            }

            @Override
            public void onFailure(Throwable t) {
                view.hideProgressBar();
                view.showToast(t.getMessage());
            }
        });
    }

    public void deleteComment(int commentId) {
        model.deleteComment(commentId, new OnFinishedListener() {
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
