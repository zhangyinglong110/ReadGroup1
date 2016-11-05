package com.example.readgroup;

import android.content.Intent;

import com.example.apphx.HxBaseApplication;

/**
 * Created by Administrator on 2016/11/5 0005.
 */

public class ReadGroupApplication extends HxBaseApplication {
    //如果账号起了冲突，则直接退出，重新进入登录页面
    @Override
    protected void exit() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
