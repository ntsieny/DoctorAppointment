package com.bigblackboy.doctorappointment.model;

import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.pojos.springpojos.Comment;
import com.bigblackboy.doctorappointment.pojos.springpojos.CommentResponse;
import com.bigblackboy.doctorappointment.utils.ErrorTranslator;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentModel {

    private SpringApi springApi;

    public CommentModel() {
        springApi = SpringController.getApi();
    }

    public void getComments(int reviewId, final OnFinishedListener onFinishedListener) {
        springApi.getComments(reviewId).enqueue(new Callback<List<CommentResponse>>() {
            @Override
            public void onResponse(Call<List<CommentResponse>> call, Response<List<CommentResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        List<CommentResponse> comments = response.body();
                        onFinishedListener.onFinished(comments);
                    } else onFinishedListener.onFailure(new Throwable(ErrorTranslator.getDescription(ErrorTranslator.COMMENTS_NOT_FOUND)));
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        onFinishedListener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        onFinishedListener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CommentResponse>> call, Throwable t) {
                onFinishedListener.onFailure(new Throwable(t));
            }
        });
    }

    public void sendComment(Comment comment, final com.bigblackboy.doctorappointment.model.OnFinishedListener onFinishedListener) {
        springApi.createComment(comment).enqueue(new Callback<com.bigblackboy.doctorappointment.pojos.springpojos.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Response<com.bigblackboy.doctorappointment.pojos.springpojos.Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) onFinishedListener.onFinished();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        onFinishedListener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        onFinishedListener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    public void sendCommentLike(String serviceId, int commentId, final com.bigblackboy.doctorappointment.model.OnFinishedListener onFinishedListener) {
        springApi.likeComment(serviceId, commentId).enqueue(new Callback<com.bigblackboy.doctorappointment.pojos.springpojos.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Response<com.bigblackboy.doctorappointment.pojos.springpojos.Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) onFinishedListener.onFinished();
                    else onFinishedListener.onFailure(new Throwable("Лайк не отправлен"));
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        onFinishedListener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        onFinishedListener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    public void deleteCommentLike(String serviceId, int commentId, final com.bigblackboy.doctorappointment.model.OnFinishedListener onFinishedListener) {
        springApi.deletelikeComment(serviceId, commentId).enqueue(new Callback<com.bigblackboy.doctorappointment.pojos.springpojos.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Response<com.bigblackboy.doctorappointment.pojos.springpojos.Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) onFinishedListener.onFinished();
                    else onFinishedListener.onFailure(new Throwable("Лайк/дизлайк не удален"));
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        onFinishedListener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        onFinishedListener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    public void sendCommentDislike(String serviceId, int commentId, final com.bigblackboy.doctorappointment.model.OnFinishedListener onFinishedListener) {
        springApi.dislikeComment(serviceId, commentId).enqueue(new Callback<com.bigblackboy.doctorappointment.pojos.springpojos.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Response<com.bigblackboy.doctorappointment.pojos.springpojos.Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) onFinishedListener.onFinished();
                    else onFinishedListener.onFailure(new Throwable("Дизлайк не отправлен"));
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        onFinishedListener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        onFinishedListener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    public void deleteComment(int commentId, final com.bigblackboy.doctorappointment.model.OnFinishedListener onFinishedListener) {
        springApi.deleteComment(commentId).enqueue(new Callback<com.bigblackboy.doctorappointment.pojos.springpojos.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Response<com.bigblackboy.doctorappointment.pojos.springpojos.Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) onFinishedListener.onFinished();
                    else onFinishedListener.onFailure(new Throwable("Комментарий не удален"));
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        onFinishedListener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        onFinishedListener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }

    public interface OnFinishedListener {
        void onFinished(List<CommentResponse> comments);

        void onFailure(Throwable t);
    }
}
