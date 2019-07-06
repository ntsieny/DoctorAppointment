package com.bigblackboy.doctorappointment.model;

import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.pojos.springpojos.ReviewsResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewModel {

    private SpringApi springApi;

    public ReviewModel() {
        springApi = SpringController.getApi();
    }

    public void getReviewById(int reviewId, final OnGetReviewListener listener) {
        springApi.getReview(reviewId).enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReviewsResponse review = response.body();
                    listener.onFinished(review);
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        listener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        listener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public void sendReviewLike(String serviceId, int reviewId, final OnFinishedListener listener) {
        springApi.likeReview(serviceId, reviewId).enqueue(new Callback<com.bigblackboy.doctorappointment.pojos.springpojos.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Response<com.bigblackboy.doctorappointment.pojos.springpojos.Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) listener.onFinished();
                    else listener.onFailure(new Throwable("Лайк не отправлен"));
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        listener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        listener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public void sendReviewDislike(String serviceId, int reviewId, final OnFinishedListener listener) {
        springApi.dislikeReview(serviceId, reviewId).enqueue(new Callback<com.bigblackboy.doctorappointment.pojos.springpojos.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Response<com.bigblackboy.doctorappointment.pojos.springpojos.Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) listener.onFinished();
                    else listener.onFailure(new Throwable("Дизлайк не отправлен"));
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        listener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        listener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public void deleteReviewLike(String serviceId, int reviewId, final OnFinishedListener listener) {
        springApi.deletelikeReview(serviceId, reviewId).enqueue(new Callback<com.bigblackboy.doctorappointment.pojos.springpojos.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Response<com.bigblackboy.doctorappointment.pojos.springpojos.Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) listener.onFinished();
                    else listener.onFailure(new Throwable("Лайк/дизлайк не удален"));
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        listener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        listener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public void getDoctorReviews(int doctorId, final OnGetDoctorReviewsListener listener) {
        springApi.getReviews(doctorId).enqueue(new Callback<List<ReviewsResponse>>() {
            @Override
            public void onResponse(Call<List<ReviewsResponse>> call, Response<List<ReviewsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if(response.body().size() > 0) {
                        List<ReviewsResponse> reviews = response.body();
                        listener.onFinished(reviews);
                    } else listener.onFailure(new Throwable("Отзывы не найдены"));
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        listener.onFailure(new Throwable(error.getString("message")));
                    } catch (Exception e) {
                        listener.onFailure(new Throwable(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ReviewsResponse>> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }



    public interface OnGetReviewListener {
        void onFinished(ReviewsResponse review);

        void onFailure(Throwable t);
    }

    public interface OnGetDoctorReviewsListener {
        void onFinished(List<ReviewsResponse> reviews);

        void onFailure(Throwable t);
    }
}
