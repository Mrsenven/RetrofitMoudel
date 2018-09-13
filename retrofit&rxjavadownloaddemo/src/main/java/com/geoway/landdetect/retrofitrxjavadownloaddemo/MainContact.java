package com.geoway.landdetect.retrofitrxjavadownloaddemo;

import com.geoway.landdetect.retrofitrxjavadownloaddemo.base.IPersenter;
import com.geoway.landdetect.retrofitrxjavadownloaddemo.base.IView;

/**
 * Created by yaoke on 2018/9/6.
 */

public interface MainContact {
    interface MainView extends IView {
        void updataProgress(int progress);

        void onComplete();

        void onStartDown();

        void chageState(int state);

        boolean checkPermission(String permission);
    }

    interface MainPersenter extends IPersenter<MainView> {

        void startDownload();

        void pause();

        void stop();
    }
}
