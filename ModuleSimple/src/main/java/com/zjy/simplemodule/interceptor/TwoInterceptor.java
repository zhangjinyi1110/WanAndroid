package com.zjy.simplemodule.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TwoInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        for (String s : chain.proceed(request).headers("Set-Cookie")) {
            Log.e(getClass().getSimpleName(), "intercept: " + s);
        }
//        request = request.newBuilder().post(request.body()).addHeader("Set-Cookie", "cookie").build();
        return chain.proceed(request);
    }
}
