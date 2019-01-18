package com.zjy.simplemodule.interceptor;

import android.content.Context;
import android.util.Log;

import com.zjy.simplemodule.base.Contracts;
import com.zjy.simplemodule.utils.DiskCache;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Response;

public class AddCookieInterceptor implements Interceptor {

    private Context context;

    public AddCookieInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        List<String> cookies = response.headers("Set-Cookie");
        for (String s : cookies) {
            Log.e(getClass().getSimpleName(), "intercept: " + s);
        }
        DiskCache.with(context).cachePath(Contracts.COOKIE_PATH).save(Contracts.COOKIE_NAME, cookies);
        return response;
    }
}
