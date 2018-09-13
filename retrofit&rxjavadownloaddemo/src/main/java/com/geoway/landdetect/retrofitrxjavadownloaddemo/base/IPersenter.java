package com.geoway.landdetect.retrofitrxjavadownloaddemo.base;

/**
 * Created by yaoke on 2018/9/6.
 */

public interface IPersenter<V extends IView> {
    void bindView(V view);

    void unBindView();

    V getView();

    boolean checkView();
}
