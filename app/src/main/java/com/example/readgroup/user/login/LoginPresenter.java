package com.example.readgroup.user.login;

import android.support.annotation.NonNull;

import com.example.apphx.model.HxUserManager;
import com.example.apphx.basemvp.MvpPresenter;
import com.example.apphx.model.event.HxErrorEvent;
import com.example.apphx.model.event.HxEventType;
import com.example.apphx.model.event.HxSimpleEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2016/10/29 0029.
 */

public class LoginPresenter extends MvpPresenter<LoginView> {
    //显示一个空视图
    @Override
    public LoginView getNullObject() {
        return LoginView.NULL;
    }

    /**
     * 登录协调人主要做的协调工作
     *
     * @param userId
     * @param userPwd
     */
    public void login(@NonNull final String userId, @NonNull final String userPwd) {
        //协调视图的一个变化
        getView().showLoading();
        //安排业务人员去做事，等待返回结果
        HxUserManager.getInstance().asyncLogin(userId, userPwd);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSimpleEvent event) {
        //判断是否是登录成功事件
        if (event.type != HxEventType.LOGIN) return;
        //协调视图的变化
        getView().hideLoading();
        getView().navigateToHome();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent event) {
        //判断是否是登录成功事件
        if (event.type != HxEventType.LOGIN) return;
        getView().hideLoading();
        String msg = String.format("失败原因：%s", event.errorMessage);
        getView().showMessage(msg);
    }


}
