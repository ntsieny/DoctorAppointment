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

    @GET("user/{serviceId}")
    Call<User> getUserByServiceId(@Path("serviceId") String serviceId);

    @POST("user/get")
    Call<User> getUserByLoginPassword(@Body User user);

    @POST("user/")
    Call<Response> createUser(@Body User user);

    @PUT("user/")
    Call<Response> updateUser(@Body User user);

    @DELETE("user/{serviceId}")
    Call<Response> deleteUser(@Path("serviceId") String serviceId);

    @POST("checklogin/")
    Call<Response> checkLoginUnique(@Body User user);

    @GET("review/{reviewId}")
    Call<ReviewsResponse> getReview(@Path("reviewId") int reviewId);

    @GET("review/doctor/{doctorId}")
    Call<List<ReviewsResponse>> getReviews(@Path("doctorId") int doctorId);

    @GET("review/user/{serviceId}")
    Call<List<Review>> getReviews(@Path("serviceId") String serviceId);

    @POST("review/create")
    Call<Response> createReview(@Body Review review);

    @PUT("review/update")
    Call<Response> updateReview(@Body Review review);

    @DELETE("review/{reviewId}")
    Call<Response> deleteReview(@Path("reviewId") int reviewId);

    @POST("review/like/{serviceId}/{reviewId}")
    Call<Response> likeReview(@Path("serviceId") String serviceId, @Path("reviewId") int reviewId);

    @POST("review/dislike/{serviceId}/{reviewId}")
    Call<Response> dislikeReview(@Path("serviceId") String serviceId, @Path("reviewId") int reviewId);

    @POST("review/deletelike/{serviceId}/{reviewId}")
    Call<Response> deletelikeReview(@Path("serviceId") String serviceId, @Path("reviewId") int reviewId);

    @GET("comment/{commentId}")
    Call<CommentResponse> getComment(@Path("commentId") int commentId);

    @GET("comment/review/{reviewId}")
    Call<List<CommentResponse>> getComments(@Path("reviewId") int reviewId);

    @GET("comment/user/{serviceId}")
    Call<List<MyCommentsResponse>> getComments(@Path("serviceId") String serviceId);

    @POST("comment/create")
    Call<Response> createComment(@Body Comment comment);

    @PUT("comment/update")
    Call<Response> updateComment(@Body Comment comment);

    @DELETE("comment/{commentId}")
    Call<Response> deleteComment(@Path("commentId") int commentId);

    @POST("comment/like/{serviceId}/{commentId}")
    Call<Response> likeComment(@Path("serviceId") String serviceId, @Path("commentId") int commentId);

    @POST("comment/dislike/{serviceId}/{commentId}")
    Call<Response> dislikeComment(@Path("serviceId") String serviceId, @Path("commentId") int commentId);

    @POST("comment/deletelike/{serviceId}/{commentId}")
    Call<Response> deletelikeComment(@Path("serviceId") String serviceId, @Path("commentId") int commentId);

    @GET("app/{appId}")
    Call<Appointment> getAppointment(@Path("appId") int appId);

    @GET("app/user/{serviceId}")
    Call<List<Appointment>> getAppointments(@Path("serviceId") String serviceId);

    @POST("app/create")
    Call<Response> createAppointment(@Body Appointment app);

    @PUT("app/update")
    Call<Response> updateAppointment(@Body Appointment app);

    @DELETE("app/{appId}")
    Call<Response> deleteAppointment(@Path("appId") int appId);

    @GET("health/{age}")
    Call<HealthInfo> getHealthInfo(@Path("age") int age);
}
