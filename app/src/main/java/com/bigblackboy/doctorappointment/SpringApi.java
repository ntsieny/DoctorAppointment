package com.bigblackboy.doctorappointment;

import com.bigblackboy.doctorappointment.api.AppointmentListApiResponse;
import com.bigblackboy.doctorappointment.api.CheckPatientApiResponse;
import com.bigblackboy.doctorappointment.api.DoctorsApiResponse;
import com.bigblackboy.doctorappointment.api.HospitalApiResponse;
import com.bigblackboy.doctorappointment.api.SpecialitiesApiResponse;
import com.bigblackboy.doctorappointment.springserver.Response;
import com.bigblackboy.doctorappointment.springserver.springmodel.Comment;
import com.bigblackboy.doctorappointment.springserver.springmodel.CommentResponse;
import com.bigblackboy.doctorappointment.springserver.springmodel.Review;
import com.bigblackboy.doctorappointment.springserver.springmodel.ReviewResponse;
import com.bigblackboy.doctorappointment.springserver.springmodel.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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

    @GET("review/get/{doctorId}")
    Call<List<ReviewResponse>> getReviews(@Path("doctorId") int doctorId);

    @POST("comment/create")
    Call<Response> createComment(@Body Comment comment);

    @GET("comment/get/comment_id/{commentId}")
    Call<CommentResponse> getComment(@Path("commentId") int commentId);

    @GET("comment/get/review_id/{reviewId}")
    Call<List<CommentResponse>> getComments(@Path("reviewId") int reviewId);

    @POST("comment/update")
    Call<Response> updateComment(@Body Comment comment);

    @DELETE("comment/delete/{commentId}")
    Call<Response> deleteComment(@Path("commentId") int commentId);

    @POST("comment/like/{serviceId}/{commentId}")
    Call<Response> likeComment(@Path("serviceId") String serviceId, @Path("commentId") int commentId);

    @POST("comment/dislike/{serviceId}/{commentId}")
    Call<Response> dislikeComment(@Path("serviceId") String serviceId, @Path("commentId") int commentId);
}
