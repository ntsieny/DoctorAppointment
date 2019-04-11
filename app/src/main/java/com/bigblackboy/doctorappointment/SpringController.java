package com.bigblackboy.doctorappointment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpringController {

    static final String BASE_URL = "https://doctor-appointment-server.herokuapp.com/api/";

    public static SpringApi getApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "*/*")
                                .addHeader("X-Requested-With", "XMLHttpRequest")
                                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                                .addHeader("DNT", "1")
                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                                .build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .baseUrl(BASE_URL)
                            //.client(httpClient.build())
                            .build();

        SpringApi springApi = retrofit.create(SpringApi.class);
        return springApi;
    }
}
