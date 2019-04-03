package com.bigblackboy.doctorappointment;

import com.bigblackboy.doctorappointment.api.AppointmentListApiResponse;
import com.bigblackboy.doctorappointment.api.CheckPatientApiResponse;
import com.bigblackboy.doctorappointment.api.DoctorsApiResponse;
import com.bigblackboy.doctorappointment.api.HospitalApiResponse;
import com.bigblackboy.doctorappointment.api.SpecialitiesApiResponse;
import com.bigblackboy.doctorappointment.springserver.Response;
import com.bigblackboy.doctorappointment.springserver.springmodel.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SpringApi {

    @POST("user/create/")
    Call<Response> createUser(@Body User user);

    @POST("checklogin/{login}")
    Call<Response> checkLoginUnique(@Path("login") String login);
}
