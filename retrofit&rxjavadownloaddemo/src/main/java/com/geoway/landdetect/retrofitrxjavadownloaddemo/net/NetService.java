package com.geoway.landdetect.retrofitrxjavadownloaddemo.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by yaoke on 2018/9/6.
 */

public interface NetService {
    // 把数据返回体拿回来
    @Streaming
    @GET
    Observable<ResponseBody> downLoadFile(@Header("RANGE") String downParam, @Url String url);


    @Streaming
    @GET
        //downParam下载参数，传下载区间使用
        //url 下载链接
    Observable<ResponseBody> download(@Header("RANGE") String downParam, @Url String url);
}
