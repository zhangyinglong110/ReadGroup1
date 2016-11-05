package com.example.readgroup.user.login;

import com.example.apphx.basemvp.MvpView;

/**
 * Created by Administrator on 2016/10/29 0029.
 */

public interface LoginView extends MvpView{

    //开始登录时的一个视图表现
    void showLoading();

    //隐藏登录成功后的视图
    void hideLoading();

    //显示登录成功的信息
    void showMessage(String msg);

    //登录成功后切换到Home界面的视图
    void navigateToHome();

    LoginView NULL = new LoginView() {
        @Override
        public void showLoading() {

        }

        @Override
        public void hideLoading() {

        }

        @Override
        public void showMessage(String msg) {

        }

        @Override
        public void navigateToHome() {

        }
    };
}
