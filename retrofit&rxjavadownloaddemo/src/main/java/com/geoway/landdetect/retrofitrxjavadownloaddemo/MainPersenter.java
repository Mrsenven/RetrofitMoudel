package com.geoway.landdetect.retrofitrxjavadownloaddemo;

import android.Manifest;
import android.os.Environment;

import com.geoway.landdetect.retrofitrxjavadownloaddemo.base.BasePersenter;
import com.geoway.landdetect.retrofitrxjavadownloaddemo.net.DownLoadListener;
import com.geoway.landdetect.retrofitrxjavadownloaddemo.net.DownLoadUtils;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by yaoke on 2018/9/6.
 */

public class MainPersenter extends BasePersenter<MainContact.MainView> implements MainContact.MainPersenter, DownLoadListener {
    private MainContact.MainView mView;
    private DownLoadUtils loadUtils;
    private Disposable disposable;

    public MainPersenter() {
        super();
        loadUtils = new DownLoadUtils(this);
    }

    @Override
    public void startDownload() {
        if (mView == null) {
            mView = getView();
        }
        if (mView.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mView.onStartDown();
            File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/Rxjava/");
            if (!file.exists()) {
                file.mkdirs();
            }
            File file1 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/Rxjava/mobileqq_android.apk");
            if (!file1.exists()) {
                try {
                    file1.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            disposable = loadUtils.download("mobileqq_android.apk", file1.getPath(), new Observer() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Object o) {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            }).subscribe(new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    mView.showToast(o.toString());
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    mView.showToast(throwable.toString());
                }
            });

            //2
//            disposable = loadUtils.download("mobileqq_android.apk", file1.getPath())
//                    .subscribe(new Consumer() {
//                        @Override
//                        public void accept(Object o) throws Exception {
//                            mView.showToast(o.toString());
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            mView.showToast(throwable.toString());
//                        }
//                    });

            //3
//            loadUtils.download(0, -1, "mobileqq_android.apk", file1, new Observer() {
//                @Override
//                public void onSubscribe(Disposable d) {
//                    Log.i("xx",d.isDisposed()+"");
//
//                }
//
//                @Override
//                public void onNext(Object o) {
//                    mView.showToast(o.toString());
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                   mView.showToast(e.toString());
//
//                }
//
//                @Override
//                public void onComplete() {
//
//                }
//            });
        }

//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                int cit = 0;
//                try {
//                    while (cit <= 100) {
//                        cit += 5;
//                        onProgress(cit);
//                        Thread.sleep(500);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    @Override
    public void pause() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        mView.onComplete();
    }

    @Override
    public void stop() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onProgress(int progess) {
        mView.updataProgress(progess);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(String error) {
        mView.showToast(error);
    }
}
