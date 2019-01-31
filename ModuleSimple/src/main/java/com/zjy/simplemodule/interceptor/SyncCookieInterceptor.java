package com.zjy.simplemodule.interceptor;

import android.content.Context;
import android.util.Log;

import com.zjy.simplemodule.base.Contracts;
import com.zjy.simplemodule.utils.DiskCache;
import com.zjy.simplemodule.utils.SharedPreferencesUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class SyncCookieInterceptor implements Interceptor {

    private Context context;

    public SyncCookieInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        List<String> headers;
//        Boolean flag = DiskCache.with(context).cachePath(Contracts.COOKIE_PATH).getAndClose(Contracts.COOKIE_SYNC);
        Boolean flag = SharedPreferencesUtils.with(context).getBoolean(Contracts.COOKIE_SYNC);
        if (flag != null && flag) {
            Response response = chain.proceed(request);
            headers = response.headers("Set-Cookie");
//            DiskCache.with(context).cachePath(Contracts.COOKIE_PATH)
//                    .saveAndClose(Contracts.COOKIE_NAME, headers);
            Set<String> stringSet = new HashSet<>(headers);
            SharedPreferencesUtils.with(context).put(Contracts.COOKIE_NAME, stringSet);
            return response;
        } else {
//            headers = DiskCache.with(context).cachePath(Contracts.COOKIE_PATH).getAndClose(Contracts.COOKIE_NAME);
            headers = new ArrayList<>(SharedPreferencesUtils.with(context).getStringSet(Contracts.COOKIE_NAME));
        }
        if (headers == null) {
            headers = new ArrayList<>();
        }
        Request.Builder builder = request.newBuilder();
//        if (headers.size() != 0)
//            builder.removeHeader("Set-Cookie");
        for (String header : headers) {
            builder.addHeader("Cookie", header);
        }
        return chain.proceed(builder.build());
    }
}
