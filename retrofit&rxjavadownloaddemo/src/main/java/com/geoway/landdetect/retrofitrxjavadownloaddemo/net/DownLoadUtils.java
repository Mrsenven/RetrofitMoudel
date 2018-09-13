package com.geoway.landdetect.retrofitrxjavadownloaddemo.net;

import android.support.annotation.NonNull;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Created by yaoke on 2018/9/6.
 */

public class DownLoadUtils {
    private static final int CONNECT_TIME = 15;

    private OkHttpClient mClient;
    Retrofit retrofit;
    private DownLoadListener mListener;

    public DownLoadUtils(DownLoadListener listener) {
        this.mListener = listener;
        mClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
//                .addInterceptor(new JsDownloadInterceptor(listener))
                .connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(mClient)
                .baseUrl("https://qd.myapp.com/myapp/qqteam/AndroidQQ/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Observable download(final String url, final String filePath, final Observer observer) {
        return retrofit.create(NetService.class)
                .downLoadFile("bytes=" + 0 + "-" + "",url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ResponseBody>() {
                    @Override
                    public ResponseBody apply(ResponseBody body) throws Exception {
                        Log.i("thread1--->", Thread.currentThread().getName());
                        return body;
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody body) throws Exception {
//                        writeFile(body.byteStream(), filePath, body.contentLength());
                        try {
                            File file =new File(filePath);
                            RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
                            randomFile.setLength(body.contentLength());
                            long one = body.contentLength() / 3;
                            download(0, one, url, file, observer)
                                    .mergeWith(download(one, one * 2, url, file, observer))
                                    .mergeWith(download(one * 2, body.contentLength(), url, file, observer))
                                    .subscribe(observer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 将输入流写入文件
     *
     * @param inputString
     * @param filePath
     */
    private void writeFile(InputStream inputString, String filePath, long length) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            Log.i("thread2--->", Thread.currentThread().getName());
            byte[] b = new byte[1024];
            int len;
            long total = 0;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b, 0, len);
                total += len;
                mListener.onProgress((int) (total * 100 / length));
                Log.w("download--->", "total-->" + total + "--progress-->" + (int) (total * 100 / length) + "--length-->" + length);
            }
            inputString.close();
            fos.close();
            mListener.onComplete();
        } catch (FileNotFoundException e) {
            mListener.onError("FileNotFoundException");
        } catch (IOException e) {
            mListener.onError("IOException");
        }

    }


    public Observable download(@NonNull final long start, @NonNull final long end, @NonNull final String url, final File file, final Observer subscriber) {
        String str = "";
        if (end == -1) {
            str = "";
        } else {
            str = end + "";
        }
        return retrofit.create(NetService.class)
                .download("bytes=" + start + "-" + str, url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ResponseBody>() {
                    @Override
                    public ResponseBody apply(ResponseBody body) throws Exception {
                        return body;
                    }
                }).observeOn(Schedulers.computation())
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        //第一次请求全部文件长度
                        if (end == -1) {
                            try {
                                RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
                                randomFile.setLength(responseBody.contentLength());
                                long one = responseBody.contentLength() / 3;
                                download(0, one, url, file, subscriber)
                                        .mergeWith(download(one, one * 2, url, file, subscriber))
                                        .mergeWith(download(one * 2, responseBody.contentLength(), url, file, subscriber))
                                        .subscribe(subscriber);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
//                            FileUtils fileUtils = new FileUtils();
                            writeFile(start, end, responseBody.byteStream(), file);
//                            writeFile(responseBody.byteStream(), file,end);
                        }
                    }
                }).subscribeOn(AndroidSchedulers.mainThread());
    }

    private void writeFile(long start,long end,InputStream inputString, File file) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rwd");
            raf.seek(start);
            Log.i("thread2--->", Thread.currentThread().getName());
            byte[] b = new byte[1024];
            int len;
            long total = 0;
            while ((len = inputString.read(b)) != -1) {
                raf.write(b, 0, len);
                total += len;
//                mListener.onProgress((int) (total * 100 / length));
                Log.w("download--->", "total-->" + total + "--progress-->" + (int) (total * 100 / end) + "--length-->" + end);
            }
            inputString.close();
            raf.close();
            mListener.onComplete();
        } catch (FileNotFoundException e) {
            mListener.onError("FileNotFoundException");
        } catch (IOException e) {
            mListener.onError("IOException");
        }

    }

    private void writeFile(InputStream inputString, File file,long length) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            Log.i("thread2--->", Thread.currentThread().getName());
            byte[] b = new byte[1024];
            int len;
            long total = 0;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b, 0, len);
                total += len;
//                mListener.onProgress((int) (total * 100 / length));
                Log.w("download--->", "total-->" + total + "--progress-->" + (int) (total * 100 / length) + "--length-->" + length);
            }
            inputString.close();
            fos.close();
            mListener.onComplete();
        } catch (FileNotFoundException e) {
            mListener.onError("FileNotFoundException");
        } catch (IOException e) {
            mListener.onError("IOException");
        }

    }


}
