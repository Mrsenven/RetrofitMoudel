package com.geoway.landdetect.retrofitrxjavadownloaddemo.net;

/**
 * Created by yaoke on 2018/9/6.
 */

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Description: 带进度 下载  拦截器
 */
public class JsDownloadInterceptor implements Interceptor {

    private DownLoadListener downloadListener;

    public JsDownloadInterceptor(DownLoadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().body(
                new JsResponseBody(response.body(), downloadListener)).build();
    }
}
