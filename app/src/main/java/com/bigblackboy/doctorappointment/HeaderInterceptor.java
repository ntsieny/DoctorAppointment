package com.bigblackboy.doctorappointment;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Cookie", "_ym_uid=15360594171004491340; _ym_d=1536059417; _ym_isad=1; jv_enter_ts_Tmyx3k0Kz1=1541521092952; jv_visits_count_Tmyx3k0Kz1=20; jv_utm_Tmyx3k0Kz1=; jv_pages_count_Tmyx3k0Kz1=1; sessionid=5bz67vxwt9z5h3d52ykk4ruyao2tcfru")
                .build();
        Response response = chain.proceed(request);
        return response;
    }
}
