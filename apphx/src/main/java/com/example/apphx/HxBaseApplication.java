package com.example.apphx;

import android.app.Application;
import android.widget.Toast;

import com.example.apphx.model.event.HxDisconnecEvent;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 环信相关基础配置
 * 作者：yuanchao on 2016/10/11 0011 11:22
 * 邮箱：yuanchao@feicuiedu.com
 */
public abstract class HxBaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化环信sdk和easeui库
        initEaseUI();
    }

    private void initEaseUI() {
        EMOptions options = new EMOptions();
        options.setAutoLogin(false); // 关闭自动登录
        options.setAcceptInvitationAlways(true); // 自动同意
        EaseUI.getInstance().init(this, options);
        // 关闭环信日志
        EMClient.getInstance().setDebugMode(false);
    }


    //处理异常等出情况
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(HxDisconnecEvent event) {
        if (event.errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
            Toast.makeText(this, R.string.hx_error_account_conflict, Toast.LENGTH_SHORT).show();
        } else if (event.errorCode == EMError.USER_REMOVED) {
            Toast.makeText(this, R.string.hx_error_account_removed, Toast.LENGTH_SHORT).show();
        }
        exit();
    }

    protected abstract void exit();
}
