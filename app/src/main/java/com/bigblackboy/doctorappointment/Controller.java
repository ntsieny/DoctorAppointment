package com.bigblackboy.doctorappointment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {

    static final String BASE_URL = "http://www.kuban-online.ru/";

    public static HospitalApi getApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        /*OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request = request.newBuilder()
                        .addHeader("Cookie", "_ym_uid=15360594171004491340; _ym_d=1536059417; _ym_isad=1; jv_enter_ts_Tmyx3k0Kz1=1541521092952; jv_visits_count_Tmyx3k0Kz1=20; jv_utm_Tmyx3k0Kz1=; jv_pages_count_Tmyx3k0Kz1=1; sessionid=5bz67vxwt9z5h3d52ykk4ruyao2tcfru")
                        .build();
                Response response = chain.proceed(request);
                return response;
            }
        });*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        HospitalApi hospitalApi = retrofit.create(HospitalApi.class);
        return hospitalApi;
    }
}
