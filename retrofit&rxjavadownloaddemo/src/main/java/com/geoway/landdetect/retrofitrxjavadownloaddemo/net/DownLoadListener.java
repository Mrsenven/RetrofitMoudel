package com.geoway.landdetect.retrofitrxjavadownloaddemo.net;

/**
 * Created by yaoke on 2018/9/6.
 */

public interface DownLoadListener {
    void onStart();

    void onPause();

    void onProgress(int progess);

    void onComplete();

    void onError(String error);
}
