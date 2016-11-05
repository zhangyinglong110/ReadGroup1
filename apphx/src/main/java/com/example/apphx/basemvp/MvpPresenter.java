package com.example.apphx.basemvp;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/10/25 0025.
 */

public abstract class MvpPresenter<V extends MvpView> {
    private V mView;

    protected final V getView(){
        return mView;
    }


    public void  onCreat(){
        EventBus.getDefault().register(this);
    }

    public void onDestory(){
        EventBus.getDefault().unregister(this);
    }


    /**
     * Presenter和视图关联
     * <p/>
     * 在Activity的onCreate()中调用
     * <p/>
     * 在Fragment的onViewCreated()或onActivityCreated()中调用
     */
    public final void atteachView(V view) {
        mView = view;
    }

    /**
     * Presenter和视图解除关联
     * <p/>
     * 在Activity的onDestroy()中调用
     * <p/>
     * 在Fragment的onViewDestroyed()中调用
     */

    public final void detachView() {
        mView = null;
        // 设置一个空对象，并不是直接把对象设置为null,只是空对象不对做任何操作。
        mView = getNullObject();
    }


    public abstract V getNullObject() ;



}
