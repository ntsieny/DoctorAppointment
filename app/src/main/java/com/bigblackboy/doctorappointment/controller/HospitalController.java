package com.bigblackboy.doctorappointment.controller;

import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HospitalController {

    static final String BASE_URL = "http://www.kuban-online.ru/";

    public static HospitalApi getApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                                .addHeader("Host", "www.kuban-online.ru")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "*/*")
                                .addHeader("Origin", "http://www.kuban-online.ru")
                                .addHeader("X-Requested-With", "XMLHttpRequest")
                                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                                .addHeader("DNT", "1")
                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .addHeader("Referer", "http://www.kuban-online.ru/signup/free/")
                                .addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                                .build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .baseUrl(BASE_URL)
                            .build();


        HospitalApi hospitalApi = retrofit.create(HospitalApi.class);
        return hospitalApi;
    }
}
