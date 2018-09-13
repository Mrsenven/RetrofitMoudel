package com.geoway.landdetect.retrofitrxjavadownloaddemo.base;

/**
 * Created by yaoke on 2018/9/6.
 */

public abstract class BasePersenter<V extends IView> implements IPersenter<V> {
    protected IView view;

    @Override
    public void bindView(V view) {
        this.view = view;
    }

    @Override
    public void unBindView() {
        view=null;
    }

    @Override
    public V getView() {
        checkView();
        return (V) view;
    }

    @Override
    public boolean checkView() {
        if (view == null) {
           new RuntimeException("no view");
        }
        return true;
    }


}
