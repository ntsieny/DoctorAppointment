package com.bigblackboy.doctorappointment.controller;

import com.bigblackboy.doctorappointment.pojos.springpojos.Response;
import com.bigblackboy.doctorappointment.pojos.springpojos.Appointment;
import com.bigblackboy.doctorappointment.pojos.springpojos.Comment;
import com.bigblackboy.doctorappointment.pojos.springpojos.CommentResponse;
import com.bigblackboy.doctorappointment.pojos.springpojos.HealthInfo;
import com.bigblackboy.doctorappointment.pojos.springpojos.MyCommentsResponse;
import com.bigblackboy.doctorappointment.pojos.springpojos.Review;
import com.bigblackboy.doctorappointment.pojos.springpojos.ReviewsResponse;
import com.bigblackboy.doctorappointment.pojos.springpojos.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SpringApi {

    @GET("user/get/{serviceId}")
    Call<User> getUserByServiceId(@Path("serviceId") String serviceId);

    @POST("user/get/")
    Call<User> getUserByLoginPassword(@Body User user);

    @POST("user/create/")
    Call<Response> createUser(@Body User user);

    @PUT("user/update/")
    Call<Response> updateUser(@Body User user);

    @DELETE("user/delete/{serviceId}")
    Call<Response> deleteUser(@Path("serviceId") String serviceId);

    @POST("checklogin/")
    Call<Response> checkLoginUnique(@Body User user);

    @GET("review/get/review_id/{reviewId}")
    Call<ReviewsResponse> getReview(@Path("reviewId") int reviewId);

    @GET("review/get/doctor_id/{doctorId}")
    Call<List<ReviewsResponse>> getReviews(@Path("doctorId") int doctorId);

    @GET("review/get/service_id/{serviceId}")
    Call<List<Review>> getReviews(@Path("serviceId") String serviceId);

    @POST("review/create")
    Call<Response> createReview(@Body Review review);

    @POST("review/update")
    Call<Response> updateReview(@Body Review review);

    @DELETE("review/delete/{reviewId}")
    Call<Response> deleteReview(@Path("reviewId") int reviewId);

    @POST("review/like/{serviceId}/{reviewId}")
    Call<Response> likeReview(@Path("serviceId") String serviceId, @Path("reviewId") int reviewId);

    @POST("review/dislike/{serviceId}/{reviewId}")
    Call<Response> dislikeReview(@Path("serviceId") String serviceId, @Path("reviewId") int reviewId);

    @POST("review/deletelike/{serviceId}/{reviewId}")
    Call<Response> deletelikeReview(@Path("serviceId") String serviceId, @Path("reviewId") int reviewId);

    @GET("comment/get/comment_id/{commentId}")
    Call<CommentResponse> getComment(@Path("commentId") int commentId);

    @GET("comment/get/review_id/{reviewId}")
    Call<List<CommentResponse>> getComments(@Path("reviewId") int reviewId);

    @GET("comment/get/service_id/{serviceId}")
    Call<List<MyCommentsResponse>> getComments(@Path("serviceId") String serviceId);

    @POST("comment/create")
    Call<Response> createComment(@Body Comment comment);

    @POST("comment/update")
    Call<Response> updateComment(@Body Comment comment);

    @DELETE("comment/delete/{commentId}")
    Call<Response> deleteComment(@Path("commentId") int commentId);

    @POST("comment/like/{serviceId}/{commentId}")
    Call<Response> likeComment(@Path("serviceId") String serviceId, @Path("commentId") int commentId);

    @POST("comment/dislike/{serviceId}/{commentId}")
    Call<Response> dislikeComment(@Path("serviceId") String serviceId, @Path("commentId") int commentId);

    @POST("comment/deletelike/{serviceId}/{commentId}")
    Call<Response> deletelikeComment(@Path("serviceId") String serviceId, @Path("commentId") int commentId);

    @GET("app/get/app_id/{appId}")
    Call<Appointment> getAppointment(@Path("appId") int appId);

    @GET("app/get/service_id/{serviceId}")
    Call<List<Appointment>> getAppointments(@Path("serviceId") String serviceId);

    @POST("app/create")
    Call<Response> createAppointment(@Body Appointment app);

    @POST("app/update")
    Call<Response> updateAppointment(@Body Appointment app);

    @DELETE("app/delete/{appId}")
    Call<Response> deleteAppointment(@Path("appId") int appId);

    @GET("health/get/age/{age}")
    Call<HealthInfo> getHealthInfo(@Path("age") int age);
}
